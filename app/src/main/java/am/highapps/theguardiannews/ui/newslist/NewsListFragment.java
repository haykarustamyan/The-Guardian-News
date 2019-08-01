package am.highapps.theguardiannews.ui.newslist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.ui.adapter.NewsListPagedAdapter;
import am.highapps.theguardiannews.ui.adapter.PinnedItemsAdapter;
import am.highapps.theguardiannews.ui.adapter.viewholder.NewsEntityPagedViewHolder;
import am.highapps.theguardiannews.ui.adapter.viewholder.PinnedNewsItemViewHolder;
import am.highapps.theguardiannews.ui.base.BaseFragment;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.ui.newsitem.NewsItemActivity;
import am.highapps.theguardiannews.util.NetworkUtil;

import static am.highapps.theguardiannews.util.Constant.EXTRA_NEWS_ID;
import static am.highapps.theguardiannews.util.Constant.INT_KEY;
import static am.highapps.theguardiannews.util.Constant.INT_KEY_OFFLINE;
import static am.highapps.theguardiannews.util.Constant.PREF_NEWEST_ARTICLE_PUBLICATION_DATE;
import static am.highapps.theguardiannews.util.Constant.SPAN_COUNT;
import static androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS;

public class NewsListFragment extends BaseFragment implements View.OnClickListener, NewsListPagedAdapter.NewsItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, PinnedItemsAdapter.OnPinNewItemClickListener {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    NetworkUtil networkUtil;

    private RecyclerView recyclerView;
    private RecyclerView pinedRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton switchFab;
    private NewsListPagedAdapter newsListPagedAdapter;
    private PinnedItemsAdapter pinnedItemsAdapter;
    private NewsListViewModel mMainViewModel;

    private StaggeredGridLayoutManager gridLayoutManager;

    private Handler handler = new Handler();
    private Runnable periodicUpdate;

    private LiveData<PagedList<NewsResponseEntity>> newsPagedList;
    private LiveData<PagedList<NewsResponseEntity>> offlineNewsPagedList;
    private long lastClickTime = 0;
    private boolean isUpdated = false;


    Observer<PagedList<NewsResponseEntity>> pagedListObserver = new Observer<PagedList<NewsResponseEntity>>() {
        @Override
        public void onChanged(@Nullable PagedList<NewsResponseEntity> pagedList) {
            if (pagedList != null) {
                newsListPagedAdapter.submitList(pagedList);
                hideRefresh();
            }
        }
    };

    Observer<PagedList<NewsResponseEntity>> offlinePagedListObserver = new Observer<PagedList<NewsResponseEntity>>() {
        @Override
        public void onChanged(@Nullable PagedList<NewsResponseEntity> pagedList) {
            if (pagedList != null) {
                newsListPagedAdapter.submitList(pagedList);
                hideRefresh();
            }
        }
    };

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PagedList<NewsResponseEntity> list = newsListPagedAdapter.getCurrentList();
        if (list == null) {
            return;
        }
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        final int[] targetPosition = layoutManager.findFirstVisibleItemPositions(new int[2]);
        int absolutePosition = targetPosition[0] + list.getPositionOffset();
        outState.putInt(INT_KEY, absolutePosition);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        findViews(view);
        initRecycler();
        initPinedRecycler();
        setListeners();
        return view;
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.rv_news_list);
        pinedRecyclerView = view.findViewById(R.id.rv_pinned);
        swipeRefreshLayout = view.findViewById(R.id.srl_news_list);
        switchFab = view.findViewById(R.id.fab_list_switch);
    }

    private void initRecycler() {
        int spanCount = sharedPreferences.getInt(SPAN_COUNT, 2);
        gridLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.getItemAnimator().setChangeDuration(500);

        swipeRefreshLayout.setOnRefreshListener(this);
        newsListPagedAdapter = new NewsListPagedAdapter();
        newsListPagedAdapter.setNewsItemClickListener(this);
        recyclerView.setAdapter(newsListPagedAdapter);
    }

    private void initPinedRecycler() {
        pinedRecyclerView.setHasFixedSize(true);
        pinedRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pinedRecyclerView.setLayoutManager(mLayoutManager);

        pinnedItemsAdapter = new PinnedItemsAdapter();
        pinnedItemsAdapter.setOnPinNewItemClickListener(this);
        pinedRecyclerView.setAdapter(pinnedItemsAdapter);
    }

    private void setListeners() {
        switchFab.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NewsListViewModel.class);
        periodicUpdate = createPeriodicRunnable(savedInstanceState);

        if (networkUtil.isConnected()) {
            observeNewsPagedList(savedInstanceState);
        } else {
            observeOfflineNewsPagedList(savedInstanceState);
            showSnackBarOffline();
        }

        handler.post(periodicUpdate);
        observePinedList();
    }

    private void observeNewsPagedList(Bundle savedInstanceState) {
        int position = 0;
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(INT_KEY);
            newsListPagedAdapter.setScrollToPosition(position);
        }
        newsPagedList = mMainViewModel.getNewsPagedList();
        isUpdated = true;
        if (offlineNewsPagedList != null) {
            offlineNewsPagedList.removeObserver(offlinePagedListObserver);
        }
        newsPagedList.observe(getViewLifecycleOwner(), pagedListObserver);
    }

    private void observeOfflineNewsPagedList(Bundle savedInstanceState) {
//        LiveData<PagedList<NewsResponseEntity>> newsPagedList;

        int position = 0;
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(INT_KEY_OFFLINE);
            newsListPagedAdapter.setScrollToPosition(position);
        }

        offlineNewsPagedList = mMainViewModel.getOfflineNewsPagedList();
//        isUpdated = false;

        if (newsPagedList != null) {
            newsPagedList.removeObserver(pagedListObserver);
        }
        offlineNewsPagedList.observe(getViewLifecycleOwner(), offlinePagedListObserver);
    }

    private void observePinedList() {
        LiveData<List<NewsResponseEntity>> entityLiveData = mMainViewModel.getPinedNews();
        entityLiveData.observe(this, new Observer<List<NewsResponseEntity>>() {
            @Override
            public void onChanged(List<NewsResponseEntity> pinedItems) {
                if (pinedItems != null) {
                    pinnedItemsAdapter.addAll(pinedItems);
                    checkPinnedItems();
                }
            }
        });
    }

    private void checkPinnedItems() {
        if (pinnedItemsAdapter.size() > 0) {
            pinedRecyclerView.smoothScrollToPosition(0);
            pinedRecyclerView.setVisibility(View.VISIBLE);
        } else {
            pinedRecyclerView.setVisibility(View.GONE);
        }
    }

    private Runnable createPeriodicRunnable(Bundle savedInstanceState) {
        return () -> {
            handler.postDelayed(periodicUpdate, 30 * 1000 - SystemClock.elapsedRealtime() % 1000);
            Log.d("testt", "periodicUpdate: " + System.currentTimeMillis());

            if (networkUtil.isConnected()) {
                if (!isUpdated) {
                    observeNewsPagedList(null);
                } else {
                    mMainViewModel.checkNewItem(sharedPreferences.getString(PREF_NEWEST_ARTICLE_PUBLICATION_DATE, null))
                            .observe(this, new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean newItemsAvailable) {
                                    if (newItemsAvailable) {
                                        mMainViewModel.invalidateDataSource();
                                    }
                                }
                            });
                }
            } else {
//                showSnackBarOffline();
            }
        };
    }

    private void hideRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showSnackBarOffline() {
        Snackbar snackbar = Snackbar.make(
                getView(),
                R.string.snackbar_offline,
                Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.WHITE);
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.show();
    }

    @Override
    public void onRefresh() {
        if (networkUtil.isConnected()) {
            if (!isUpdated) {
                observeNewsPagedList(null);
            } else {
                mMainViewModel.invalidateDataSource();
            }

        } else {
            showSnackBarOffline();
            hideRefresh();
        }
    }

    @Override
    public void onDetach() {
        handler.removeCallbacks(periodicUpdate);
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_list_switch:
                if (!((Animatable) switchFab.getDrawable()).isRunning()) {
                    if (gridLayoutManager.getSpanCount() == 1) {
                        switchFab.setImageDrawable(AnimatedVectorDrawableCompat.create(getActivity(), R.drawable.avd_grid_to_list));
                        gridLayoutManager.setSpanCount(2);
                        sharedPreferences.edit().putInt(SPAN_COUNT, 2).apply();
                    } else {
                        switchFab.setImageDrawable(AnimatedVectorDrawableCompat.create(getActivity(), R.drawable.avd_list_to_grid));
                        gridLayoutManager.setSpanCount(1);
                        sharedPreferences.edit().putInt(SPAN_COUNT, 1).apply();

                    }
                    ((Animatable) switchFab.getDrawable()).start();
                    newsListPagedAdapter.notifyItemRangeChanged(0, newsListPagedAdapter.getItemCount());
                }
                break;
        }
    }

    @Override
    public void onItemClick(NewsEntityPagedViewHolder viewHolder, NewsResponseEntity newsEntity) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        ActivityOptionsCompat bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    viewHolder.thumbnailIv,
                    viewHolder.thumbnailIv.getTransitionName()
            );
        }
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
        intent.putExtra(EXTRA_NEWS_ID, newsEntity.getId());

        startActivity(intent, bundle.toBundle());
    }

    @Override
    public void onPinNewItemClick(PinnedNewsItemViewHolder viewHolder, NewsResponseEntity newsEntity) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        ActivityOptionsCompat bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    viewHolder.thumbnailIv,
                    viewHolder.thumbnailIv.getTransitionName()
            );
        }
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
        intent.putExtra(EXTRA_NEWS_ID, newsEntity.getId());
        startActivity(intent, bundle.toBundle());
    }
}
