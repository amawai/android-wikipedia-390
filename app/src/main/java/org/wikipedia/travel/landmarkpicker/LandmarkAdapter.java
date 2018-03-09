package org.wikipedia.travel.landmarkpicker;

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

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.ViewHolder> {

    private List<LandmarkCard> landmarkCardList;
    private Context context;

    public LandmarkAdapter(List<LandmarkCard> landmarkCards, Context context) {
        this.landmarkCardList = landmarkCards;
        this.context = context;
    }

    @Override
    public LandmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_planner_places_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LandmarkAdapter.ViewHolder holder, int position) {

        LandmarkCard landmarkCard = landmarkCardList.get(position);

        holder.textViewTitle.setText(landmarkCard.getTitle());
        holder.textViewDesc.setText(landmarkCard.getDesc());
    }

    @Override
    public int getItemCount() {
        return landmarkCardList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert and remove for implementation of view closing feature
    public void insert(int position, LandmarkCard landmarkCard) {
        landmarkCardList.add(position, landmarkCard);
        notifyItemInserted(position);
    }

    public void remove(LandmarkCard landmarkCard) {
        int position = landmarkCardList.indexOf(landmarkCard);
        landmarkCardList.remove(position);
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
