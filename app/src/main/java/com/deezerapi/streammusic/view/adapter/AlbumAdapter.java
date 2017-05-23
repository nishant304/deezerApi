package com.deezerapi.streammusic.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.events.AlbumSelectedEvent;
import com.deezerapi.streammusic.model.Album;
import com.deezerapi.streammusic.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by nishant on 18.05.17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {

    private List<Album> albumList;

    private LayoutInflater layoutInflater;

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.albumList = albumList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(albumList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(layoutInflater.inflate(R.layout.album_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvAlbumName.setText(albumList.get(position).getTitle());
        Glide.with(holder.ivAlbumImage.getContext()).
                load(albumList.get(position).getCoverMedium()).into(holder.ivAlbumImage);
    }

    public void addNewAlbums(List<Album> list) {
        int pos = albumList.size();
        albumList.addAll(list);
        notifyItemRangeInserted(pos, list.size());
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivAlbumImage;
        private TextView tvAlbumName;

        Holder(View view) {
            super(view);
            view.setOnClickListener(this);
            ivAlbumImage = (ImageView) view.findViewById(R.id.ivAlbumCover);
            tvAlbumName = (TextView) view.findViewById(R.id.tvAlbumName);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            ImageView im = (ImageView) v.findViewById(R.id.ivAlbumCover);
            int id = Integer.valueOf(albumList.get(pos).getId());
            String url = albumList.get(pos).getCoverMedium();
            EventBus.getDefault().post(new AlbumSelectedEvent(im, id, url));
        }
    }

}
