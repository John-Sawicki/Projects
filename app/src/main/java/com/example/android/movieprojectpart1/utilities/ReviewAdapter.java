package com.example.android.movieprojectpart1.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieprojectpart1.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterVH>{
    String[] mReviewerNames={"Bob","The Dude"};

    private final RaOnClickHandler mRaOnClickHandler;
    public interface RaOnClickHandler{
        void onClick(String reviewerName);
    }
    public ReviewAdapter(RaOnClickHandler tClickHandler){
        mRaOnClickHandler = tClickHandler;
    }
    public class ReviewAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mReviewerTV;
        public ReviewAdapterVH(View view){
            super(view);
            mReviewerTV = view.findViewById(R.id.reviewer_name);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int trailerAdpPosition = getAdapterPosition();
            String oneReviewer = mReviewerNames[trailerAdpPosition];
            mRaOnClickHandler.onClick(oneReviewer);
        }
    }

    @Override
    public ReviewAdapterVH onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        int layoutForReviewer = R.layout.reviewer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForReviewer, parent);
        return new ReviewAdapterVH(view);
    }
    @Override
    public void onBindViewHolder(ReviewAdapterVH holder, int position) {
        //super.onBindViewHolder(holder, position);
        String mReviewName = mReviewerNames[position];
        if(mReviewName !="0")       //if this doesn't work, set view to gone
            holder.mReviewerTV.setText(mReviewName);
    }
    @Override
    public int getItemCount() {
        if (mReviewerNames==null) return 0;
        return mReviewerNames.length;
    }
    public void setReviewerNames(String[] reviewerNames){
        mReviewerNames = reviewerNames;
        notifyDataSetChanged();

    }

}
