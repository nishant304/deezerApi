package com.deezerapi.streammusic.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.model.Artist;
import com.deezerapi.streammusic.view.activity.AlbumListActivity;
import com.deezerapi.streammusic.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by nishant on 18.05.17.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Holder> {

    private List<Artist> list;

    private LayoutInflater layoutInflater;

    private Context context;

    public ArtistAdapter(Context context, List<Artist> list){
        this.list = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(layoutInflater.inflate(R.layout.artist_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.tvArtisitName.setText(list.get(position).getName());
        Glide.with(holder.ivArtistImage.getContext())
                .load(list.get(position)
                        .getPicture())
                .into(holder.ivArtistImage);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v.findViewById(R.id.ivArtist);
                EventBus.getDefault().post(new MainActivity.ArtistSelectedEvent(imageView,
                        list.get(position).getName(),list.get(position)
                        .getPicture()));
            }
        });
    }

    public void appendData(List<Artist> list){
        int initialSize = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(initialSize,list.size());
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.ivArtistImage);
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder{

        private ImageView ivArtistImage;
        private TextView tvArtisitName;

        private View view;

        Holder(View view){
            super(view);
            this.view  = view;
            ivArtistImage = (ImageView) view.findViewById(R.id.ivArtist);
            tvArtisitName = (TextView) view.findViewById(R.id.tvArtist);
        }
    }

}
