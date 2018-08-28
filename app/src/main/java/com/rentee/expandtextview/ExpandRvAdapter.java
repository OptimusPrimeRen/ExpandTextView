package com.rentee.expandtextview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * author: rentee
 * date: 2018/8/27
 * description：
 */
public class ExpandRvAdapter extends RecyclerView.Adapter<ExpandRvViewHolder> {

    private List<ExpandModel> mExpandModels;

    public void setData(List<ExpandModel> expandModels) {
        mExpandModels = expandModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpandRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        return new ExpandRvViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpandRvViewHolder holder, int position) {
        holder.updateView(mExpandModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mExpandModels.size();
    }
}
