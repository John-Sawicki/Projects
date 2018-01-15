package com.example.android.movieprojectpart1.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieprojectpart1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private String[] moviePosterUrls;
    private int[] dummyImages = {R.drawable.atari, R.drawable.dreamcast, R.drawable.gamecube, R.drawable.genesis,
            R.drawable.nes, R.drawable.nsixtyfour, R.drawable.psfour, R.drawable.wii,
            R.drawable.atari, R.drawable.dreamcast, R.drawable.gamecube, R.drawable.genesis,
            R.drawable.nes, R.drawable.nsixtyfour, R.drawable.psfour, R.drawable.wii,
            R.drawable.atari, R.drawable.dreamcast, R.drawable.gamecube, R.drawable.genesis};  //used to test grid view 20 images
    public MovieAdapter(){
    }
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int posterLayout = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttach = false;
        View view = inflater.inflate(posterLayout, viewGroup, shouldAttach);
        return new MovieAdapterViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String moviePosterUrl = moviePosterUrls[position];  Log.d("onBindVH", moviePosterUrl);
         Context context = holder.moviePosterImageView.getContext();
        Picasso.with(context)
                .load(moviePosterUrl)
                .into(holder.moviePosterImageView);
        //int consoleImage = dummyImages[position];
        //holder.moviePosterImageView.setImageResource(consoleImage);    //used to test grid layout
    }
    @Override
    public int getItemCount() {
        if(moviePosterUrls==null) return 0;
        return moviePosterUrls.length;
    }
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder{
        public final ImageView moviePosterImageView;    //set the images to this in the poster_list_item layout
        public MovieAdapterViewHolder(View view){
            super(view);
            moviePosterImageView = view.findViewById(R.id.grid_poster);
        }
    }
    public void setUrlData(String[] movieUrls){ //update data onPostExecute
        moviePosterUrls =movieUrls;
        notifyDataSetChanged(); //method of recycler view to update images
    }
}
