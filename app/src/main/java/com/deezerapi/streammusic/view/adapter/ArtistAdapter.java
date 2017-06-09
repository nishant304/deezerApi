package com.deezerapi.streammusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezerapi.streammusic.ArtistSelectedEvent;
import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.model.Artist;
import com.deezerapi.streammusic.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant on 18.05.17.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Holder> {

    public List<Artist> getList() {
        return list;
    }

    private List<Artist> list = new ArrayList<>();

    private LayoutInflater layoutInflater;

    private Context context;

    public ArtistAdapter(Context context, List<Artist> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        appendData(list);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getId() == Integer.MAX_VALUE ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1){
            return new ProgressBarHolder(layoutInflater.inflate(R.layout.progress_layout, parent, false));
        }
        return new Holder(layoutInflater.inflate(R.layout.artist_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        if(holder instanceof ProgressBarHolder){
            return;
        }

        holder.tvArtisitName.setText(list.get(position).getName());
        holder.ivArtistImage.setTransitionName("name"+position);
        Glide.with(holder.ivArtistImage.getContext())
                .load(list.get(position)
                        .getPicture())
                .into(holder.ivArtistImage);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v.findViewById(R.id.ivArtist);
                EventBus.getDefault().post(new ArtistSelectedEvent(imageView,
                        list.get(holder.getLayoutPosition()).getName(), list.get(holder.getLayoutPosition())
                        .getPicture()));
            }
        });
    }

    public void appendData(List<Artist> list) {
        int initialSize = this.list.size();
        if (initialSize != 0) {
            this.list.remove(this.list.size() - 1);
            notifyItemRemoved(initialSize - 1);
        }
        this.list.addAll(list);
        notifyItemRangeInserted(initialSize, list.size());
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        if(!(holder instanceof ProgressBarHolder)) {
            Glide.clear(holder.ivArtistImage);
        }
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void onLoadMore(){
        Artist artist = new Artist();
        artist.setId(Integer.MAX_VALUE);
        this.list.add(artist);
        notifyItemInserted(list.size()-1);
    }

    static class Holder extends RecyclerView.ViewHolder {

        private ImageView ivArtistImage;
        private TextView tvArtisitName;

        private View view;

        Holder(View view) {
            super(view);
            this.view = view;
            ivArtistImage = (ImageView) view.findViewById(R.id.ivArtist);
            tvArtisitName = (TextView) view.findViewById(R.id.tvArtist);
        }
    }

    static class ProgressBarHolder extends Holder{

        private ProgressBar progressBar;

        ProgressBarHolder(View view){
            super(view);
            this.progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

}
