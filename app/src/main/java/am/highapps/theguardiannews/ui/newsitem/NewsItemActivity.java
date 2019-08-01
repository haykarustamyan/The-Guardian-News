package am.highapps.theguardiannews.ui.newsitem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.ui.base.BaseActivity;

import static am.highapps.theguardiannews.util.Constant.EXTRA_NEWS_ID;


public class NewsItemActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private NewsItemViewModel newsItemViewModel;
    private NewsResponseEntity newsResponseEntity;

    private TextView description;
    private ImageView newsHeaderIv;
    private TextView offlineView;
    private FloatingActionButton saveFab;
    private FloatingActionButton pinFab;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private String newsId;

    @SuppressLint("RestrictedApi")
    Observer<NewsResponseEntity> itemObserver = newsResponseEntity -> {
        if (newsResponseEntity != null) {
            this.newsResponseEntity = newsResponseEntity;

            pinFab.setImageResource(newsResponseEntity.isPined() ? R.drawable.pin_off : R.drawable.pin);
            pinFab.setImageMatrix(new Matrix());

            saveFab.setImageResource(newsResponseEntity.isSaved() ? R.drawable.ic_bookmark_white_24dp : R.drawable.ic_bookmark_border_white_24dp);
            saveFab.setImageMatrix(new Matrix());

            Glide.with(this)
                    .load(newsResponseEntity.getFields() == null ? R.drawable.ic_news_placeholder : newsResponseEntity.getFields().getThumbnail())
                    .placeholder(R.drawable.ic_news_placeholder)
                    .error(R.drawable.ic_news_placeholder)
                    .centerCrop()
                    .dontAnimate()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(newsHeaderIv);

            mCollapsingToolbarLayout.setTitle(newsResponseEntity.getSectionName());
            description.setText(newsResponseEntity.getFields().getBodyText());
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_news_placeholder)
                    .placeholder(R.drawable.ic_news_placeholder)
                    .error(R.drawable.ic_news_placeholder)
                    .centerCrop()
                    .dontAnimate()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(newsHeaderIv);

            saveFab.setVisibility(View.GONE);
            pinFab.setVisibility(View.GONE);
            offlineView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_news_item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsItemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NewsItemViewModel.class);

        getData(getIntent());
        findView();
        initFields();
        setListeners();

        observeNewsItem();
    }

    private void observeNewsItem() {
        newsItemViewModel.getNewsItem(newsId).observe(this, itemObserver);
    }

    private void getData(Intent intent) {
        newsId = intent.getStringExtra(EXTRA_NEWS_ID);
    }

    private void findView() {
        mCollapsingToolbarLayout = findViewById(R.id.collaps_toolbar);
        offlineView = findViewById(R.id.view_offline);
        description = findViewById(R.id.tv_news_content);
        newsHeaderIv = findViewById(R.id.detailed_image);
        saveFab = findViewById(R.id.fab_save);
        pinFab = findViewById(R.id.fab_pin);
    }


    private void initFields() {
        supportPostponeEnterTransition();
        showActionBarIcon();
    }

    private void setListeners() {
        saveFab.setOnClickListener(this);
        pinFab.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_pin:
                if (newsResponseEntity != null) {
                    newsResponseEntity.setPined(!newsResponseEntity.isPined());
                    pinFab.setImageResource(newsResponseEntity.isPined() ? R.drawable.pin_off : R.drawable.pin);
                    // workaround bug fix  https://issuetracker.google.com/issues/111316656
                    pinFab.setImageMatrix(new Matrix());

                    newsResponseEntity.setWebPublicationDate("" + System.currentTimeMillis());
                    newsItemViewModel.setPinState(newsResponseEntity);
                    observeNewsItem();
                }
                break;
            case R.id.fab_save:
                if (newsResponseEntity != null) {
                    newsResponseEntity.setSaved(!newsResponseEntity.isSaved());
                    saveFab.setImageResource(newsResponseEntity.isSaved() ? R.drawable.ic_bookmark_white_24dp : R.drawable.ic_bookmark_border_white_24dp);
                    saveFab.setImageMatrix(new Matrix());

                    newsItemViewModel.setNewsItemSaveState(newsResponseEntity);
                    observeNewsItem();
                }
                break;
        }
    }

}





