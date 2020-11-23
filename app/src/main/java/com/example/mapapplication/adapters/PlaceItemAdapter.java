package com.example.mapapplication.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mapapplication.R;
import com.example.mapapplication.data.place.PlaceEntity;

import java.util.ArrayList;
import java.util.List;

public class PlaceItemAdapter extends RecyclerView.Adapter<PlaceItemAdapter.ViewHolder> {

    private List<PlaceEntity> mValues = new ArrayList<>();
    private OnItemCliclListener mListener;

    public void PlaceItemAdapter(List<PlaceEntity> items) {
        mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PlaceEntity currentPlaceEntity = mValues.get(position);
        holder.place_name_item.setText(currentPlaceEntity.getPlace_name());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView place_name_item;

        public ViewHolder(View view) {
            super(view);
            place_name_item = (TextView) view.findViewById(R.id.place_name_item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    if (mListener != null && index != RecyclerView.NO_POSITION){
                        mListener.onItemClick(mValues.get(index), view);
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + place_name_item.getText() + "'";
        }
    }

    public interface OnItemCliclListener {
        void onItemClick(PlaceEntity placeEntity, View view);
    }

    public void OnItemClickListener(OnItemCliclListener listener){
        this.mListener = listener;
    }
}