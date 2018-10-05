package com.ioki.key;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public ListItemAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemAdapter.ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.heading.setText(listItem.getHeading());
        holder.description.setText(listItem.getDescription());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView heading;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.listItemHeading);
            description = itemView.findViewById(R.id.listItemDescription);
        }
    }
}
