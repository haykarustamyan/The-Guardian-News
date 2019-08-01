package am.highapps.theguardiannews.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.ui.adapter.NewsListPagedAdapter;

public class NewsEntityPagedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private NewsListPagedAdapter.NewsItemClickListener newsItemClickListener;

    private NewsResponseEntity newsResponseEntity;

    private LinearLayout rootLl;
    private TextView titleTv;
    private TextView categoryTv;
    public ImageView thumbnailIv;

    public NewsEntityPagedViewHolder(View view, NewsListPagedAdapter.NewsItemClickListener newsItemClickListener) {
        super(view);
        this.newsItemClickListener = newsItemClickListener;

        findViews(view);
        setListeners();

    }

    private void setListeners() {
        rootLl.setOnClickListener(this);
    }

    private void findViews(View view) {
        rootLl = view.findViewById(R.id.root_item_news);
        titleTv = view.findViewById(R.id.tv_title);
        categoryTv = view.findViewById(R.id.tv_category);
        thumbnailIv = view.findViewById(R.id.iv_thumbnail);
    }

    public void bind(NewsResponseEntity entity) {
        if (entity == null) return;
        this.newsResponseEntity = entity;

        titleTv.setText(newsResponseEntity.getWebTitle());
        categoryTv.setText(newsResponseEntity.getSectionName());

        Glide.with(itemView.getContext())
                .load(newsResponseEntity.getFields() == null ? R.drawable.ic_news_placeholder : newsResponseEntity.getFields().getThumbnail())
                .centerCrop()
                .placeholder(R.drawable.ic_news_placeholder)
                .error(R.drawable.ic_news_placeholder)
                .into(thumbnailIv);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root_item_news:
                if (newsItemClickListener != null) {
                    newsItemClickListener.onItemClick(this, newsResponseEntity);
                }
                break;
        }
    }
}
