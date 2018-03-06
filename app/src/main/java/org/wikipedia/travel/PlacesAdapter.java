package org.wikipedia.travel;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wikipedia.R;

import java.util.List;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<PlacesCard> placesCardList;
    private Context context;

    public PlacesAdapter(List<PlacesCard> placesCards, Context context) {
        this.placesCardList = placesCards;
        this.context = context;
    }

    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_planner_places_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlacesAdapter.ViewHolder holder, int position) {

        PlacesCard placesCard = placesCardList.get(position);

        holder.textViewTitle.setText(placesCard.getTitle());
        holder.textViewDesc.setText(placesCard.getDesc());
    }

    @Override
    public int getItemCount() {
        return placesCardList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert and remove for implementation of view closing feature
    public void insert(int position, PlacesCard placesCard) {
        placesCardList.add(position, placesCard);
        notifyItemInserted(position);
    }

    public void remove(PlacesCard placesCard) {
        int position = placesCardList.indexOf(placesCard);
        placesCardList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cv;
        public TextView textViewTitle;
        public TextView textViewDesc;

        ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardView);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewDesc = (TextView) itemView.findViewById(R.id.desc);

        }
    }
}
