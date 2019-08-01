package am.highapps.theguardiannews.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.ui.adapter.PinnedItemsAdapter;

public class PinnedNewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private PinnedItemsAdapter.OnPinNewItemClickListener onPinNewItemClickListener;

    private LinearLayout rootLl;
    private TextView titleTv;
    private TextView categoryTv;
    public ImageView thumbnailIv;

    private NewsResponseEntity newsResponseEntity;

    public PinnedNewsItemViewHolder(View view, PinnedItemsAdapter.OnPinNewItemClickListener onPinNewItemClickListener) {
        super(view);
        this.onPinNewItemClickListener = onPinNewItemClickListener;

        findViews(view);
        setListeners();

    }

    private void findViews(View view) {
        rootLl = view.findViewById(R.id.root_item_news);
        titleTv = view.findViewById(R.id.tv_title);
        categoryTv = view.findViewById(R.id.tv_category);
        thumbnailIv = view.findViewById(R.id.iv_thumbnail);
    }

    private void setListeners() {
        rootLl.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.root_item_news:
                if (onPinNewItemClickListener != null) {
                    onPinNewItemClickListener.onPinNewItemClick(this, newsResponseEntity);
                }
                break;
        }
    }
}
