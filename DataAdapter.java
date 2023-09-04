package com.mod5.projecttwo;

import com.mod5.projecttwo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    // Instance variables to store the data items, database helper, and listener for the edit button clicks
    private List<DataItem> dataItems;
    private Database dbHelper;
    private OnEditClickListener onEditClickListener;

    // constructor
    public DataAdapter(Database dbHelper, OnEditClickListener onEditClickListener){
        this.dbHelper = dbHelper;
        this.dataItems = new ArrayList<>();
        this.onEditClickListener = onEditClickListener;
    }

    // Method to set data items and refresh the list
    public void setDataItems(List<DataItem> dataItems){
        this.dataItems = dataItems;
        notifyDataSetChanged();
    }

    // this method is the get the data items
    public List<DataItem> getDataItems(){
        return dataItems;
    }

    // this is the ViewHolder creation
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    // viewHolder bind
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        DataItem dataItem = dataItems.get(position);
        holder.label1.setText(dataItem.getLabel1());
        holder.label2.setText(dataItem.getLabel2());

        // adding a click listener for the editButton
        holder.editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(onEditClickListener != null){
                    onEditClickListener.onEditClick(holder.getAdapterPosition());
                }
            }
        });

        // adding a click listener for the deleteButton
        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DataItem itemToRemove = dataItems.get(holder.getAdapterPosition());
                dbHelper.deleteDataItem(itemToRemove.getId());
                dataItems.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    // Method to get the item count
    @Override
    public int getItemCount(){
        return dataItems.size();
    }

    // adding an interface to handle the click events for the edit button
    public interface OnEditClickListener{
        void onEditClick(int position);
    }

    // this is my  ViewHolder class
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView label1, label2;
        ImageButton editButton, deleteButton;

        public ViewHolder(View itemView){
            super(itemView);
            label1 = itemView.findViewById(R.id.label1);
            label2 = itemView.findViewById(R.id.label2);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
