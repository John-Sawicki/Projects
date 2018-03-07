package com.example.android.movieprojectpart1.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieprojectpart1.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterVH> {
    private String[] mTrailerUrlKeys ={"empty","empty","empty","empty","empty","empty","empty","empty","empty","empty"};

    private final TAOnClickHandler mTAOnClickHandler;
    public interface TAOnClickHandler{
        void onClick(String trailerName);
    }
    public TrailerAdapter(TAOnClickHandler tClickHandler){
        mTAOnClickHandler = tClickHandler;
    }
    public class TrailerAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mTrailerTextView;
        public TrailerAdapterVH(View view){
            super(view);
            mTrailerTextView = view.findViewById(R.id.trailer_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String selectedTrailerUrl = mTrailerUrlKeys[adapterPosition];
            mTAOnClickHandler.onClick(selectedTrailerUrl);
        }
    }
    @Override
    public TrailerAdapterVH onCreateViewHolder(ViewGroup parent, int viewType)  {
        Context context = parent.getContext();
        int layoutIdForTrailer = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForTrailer, parent, false);
        return new TrailerAdapterVH(view);
    }
    @Override
    public void onBindViewHolder(TrailerAdapterVH holder, int position) {
        String trailerUrlKey = mTrailerUrlKeys[position];
        if(trailerUrlKey !="0"){    //set to "empty" after verifying url keys
            holder.mTrailerTextView.setText(trailerUrlKey);
            //holder.mTrailerTextView.setText("Trailer "+position); //array will show Trailer 1, Trailer 2,
        }
        //the trailer array is initialized to all "0" and valid trailer keys replaces the 0. If there are 3 valid trailer keys, only 3
        // trailer will be shown and all three will have a valid value to go to youTube
    }
    @Override
    public int getItemCount() {
        if(mTrailerUrlKeys ==null) return 0;
        return mTrailerUrlKeys.length;
    }
    public void setTrailerUrlKeys(String[] trailerUrlKeys){
        mTrailerUrlKeys = trailerUrlKeys;
        notifyDataSetChanged();
    }
}
