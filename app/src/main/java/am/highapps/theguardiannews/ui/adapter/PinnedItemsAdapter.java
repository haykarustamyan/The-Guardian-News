package am.highapps.theguardiannews.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import am.highapps.theguardiannews.R;
import am.highapps.theguardiannews.data.entity.NewsResponseEntity;
import am.highapps.theguardiannews.ui.adapter.viewholder.PinnedNewsItemViewHolder;


public class PinnedItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<NewsResponseEntity> data = new ArrayList<>();

    private OnPinNewItemClickListener onPinNewItemClickListener;

    public PinnedItemsAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pined_news_entity, parent, false);
        return new PinnedNewsItemViewHolder(view, onPinNewItemClickListener);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((PinnedNewsItemViewHolder) viewHolder).bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<NewsResponseEntity> entities) {
        data.clear();
        data.addAll(entities);
        notifyDataSetChanged();
    }


    public void removeItem(NewsResponseEntity entity) {
        data.remove(entity);
        notifyDataSetChanged();
    }


    public void addItem(NewsResponseEntity entity) {
        data.add(entity);
        notifyDataSetChanged();
    }


    public int size() {
        return data.size();
    }


    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void setOnPinNewItemClickListener(OnPinNewItemClickListener onPinNewItemClickListener) {
        this.onPinNewItemClickListener = onPinNewItemClickListener;
    }

    public interface OnPinNewItemClickListener {
        void onPinNewItemClick(PinnedNewsItemViewHolder pinnedNewsItemViewHolder, NewsResponseEntity entity);
    }

}