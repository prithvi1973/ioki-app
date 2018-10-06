package com.ioki.key;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private List<ListItem> listItems;

    ListItemAdapter(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemAdapter.ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        holder.heading.setText(listItem.getHeading());
        holder.description.setText(listItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading;
        TextView description;
        ImageButton actionButton;

        ViewHolder(View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.listItemHeading);
            description = itemView.findViewById(R.id.listItemDescription);
            actionButton = itemView.findViewById(R.id.listItemActionButton);
        }
    }
}
