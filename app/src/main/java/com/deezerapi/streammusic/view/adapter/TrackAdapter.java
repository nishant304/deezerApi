package com.deezerapi.streammusic.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.model.Track;

import java.util.List;

/**
 * Created by nishant on 20.05.17.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.Holder> {

    private Context context;

    public List<Track> getTrackList() {
        return trackList;
    }

    private List<Track> trackList;

    private LayoutInflater layoutInflater;

    public TrackAdapter(Context context, List<Track> tracks){
        this.context = context;
        this.trackList = tracks;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return trackList.get(position).getType().equals("dummy")?1:0;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1){
            return new CdHolder(layoutInflater.inflate(R.layout.item_cd_layout,parent,false));
        }
        return new Holder(layoutInflater.inflate(R.layout.item_track_view,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if(holder instanceof CdHolder){
            CdHolder cdHolder = (CdHolder)holder;
            cdHolder.cdText.setText(trackList.get(position).getTitle());
            return;
        }
        holder.tvRank.setText(trackList.get(position).getTrackPosition()+"");
        holder.tvArtistName.setText(trackList.get(position).getArtist().getName());
        holder.tvTrackName.setText(trackList.get(position).getTitle());
        holder.tvTrackLength.setText(trackList.get(position).getDuration()+"");
    }

    public void appendData(List<Track> list){
        int initialSize = this.trackList.size();
        this.trackList.addAll(list);
        notifyItemRangeInserted(initialSize,list.size());
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    static class Holder extends RecyclerView.ViewHolder{

        private TextView tvRank;
        private TextView tvTrackName;
        private TextView tvArtistName;
        private TextView tvTrackLength;

        Holder(View view){
            super(view);
            tvRank =  (TextView) view.findViewById(R.id.rank);
            tvTrackName =  (TextView) view.findViewById(R.id.trackName);
            tvArtistName =  (TextView) view.findViewById(R.id.artistName);
            tvTrackLength =  (TextView) view.findViewById(R.id.trackLength);
        }
    }

    class CdHolder extends Holder{

        private TextView cdText;

        CdHolder(View view){
            super(view);
            cdText = (TextView)view.findViewById(R.id.tvCdText);
        }

    }

}
