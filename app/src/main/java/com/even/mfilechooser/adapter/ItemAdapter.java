package com.even.mfilechooser.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.even.mfilechooser.R;
import com.even.mfilechooser.interfaces.OnFileSelectedListener;
import com.even.mfilechooser.model.Item;
import java.util.List;

/**
 * ItemAdapter
 * Created by Even on 17/2/10.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private List<Item> items;
    private OnFileSelectedListener onFileSelectedListener;

    public ItemAdapter() {
    }

    public ItemAdapter(OnFileSelectedListener onFileSelectedListener) {
        this.onFileSelectedListener = onFileSelectedListener;
    }

    @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_file, parent, false));
    }

    @Override public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public OnFileSelectedListener getOnFileSelectedListener() {
        return onFileSelectedListener;
    }

    public void setOnFileSelectedListener(OnFileSelectedListener onFileSelectedListener) {
        this.onFileSelectedListener = onFileSelectedListener;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView ivFile;
        private TextView tvFileName;
        private Item item;

        public ItemHolder(View itemView) {
            super(itemView);

            ivFile = (ImageView) itemView.findViewById(R.id.iv_file);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (onFileSelectedListener != null) {
                        onFileSelectedListener.onFileSelected(item, getAdapterPosition());
                    }
                }
            });
        }

        public void setData(Item item) {
            this.item = item;
            tvFileName.setText(item.getName());
            ivFile.setImageDrawable(item.getIcon());
        }
    }
}
