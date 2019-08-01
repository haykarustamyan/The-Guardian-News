package am.highapps.theguardiannews.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.ui.adapter.viewholder.NewsEntityPagedViewHolder;

public class NewsListPagedAdapter extends PagedListAdapter<NewsResponseEntity, NewsEntityPagedViewHolder> {

    private NewsItemClickListener newsItemClickListener;
     private RecyclerView recyclerView;
    private boolean setObserved;

    private int scrollToPosition = -1;

    private static DiffUtil.ItemCallback<NewsResponseEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<NewsResponseEntity>() {
                @Override
                public boolean areItemsTheSame(NewsResponseEntity oldItem, NewsResponseEntity newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(NewsResponseEntity oldItem, NewsResponseEntity newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public NewsListPagedAdapter() {
        super(NewsListPagedAdapter.DIFF_CALLBACK);
    }

    public void setScrollToPosition(int position) {
        scrollToPosition = position;
    }

    @Override
    public void submitList(@Nullable PagedList<NewsResponseEntity> pagedList) {
        super.submitList(pagedList);
        if (pagedList != null) {
            final boolean firstSet = !setObserved;
            setObserved = true;

            if (firstSet && recyclerView != null
                    && (scrollToPosition >= 0)) {
                int localScrollToPosition = scrollToPosition - pagedList.getPositionOffset();
                recyclerView.scrollToPosition(localScrollToPosition);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = null;
    }

    @NonNull
    @Override
    public NewsEntityPagedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_entity, parent, false);
        return new NewsEntityPagedViewHolder(view, newsItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsEntityPagedViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setNewsItemClickListener(NewsItemClickListener newsItemClickListener) {
        this.newsItemClickListener = newsItemClickListener;
    }

    public interface NewsItemClickListener {
        void onItemClick(NewsEntityPagedViewHolder viewHolder,NewsResponseEntity entity);
    }

}
