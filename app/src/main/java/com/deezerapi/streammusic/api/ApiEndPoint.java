package com.deezerapi.streammusic.api;

import com.deezerapi.streammusic.model.AlbumSearchResponse;
import com.deezerapi.streammusic.model.ArtistSearchResponse;
import com.deezerapi.streammusic.model.TrackResponse;
import com.deezerapi.streammusic.network.AppResponse;

/**
 * Created by nishant on 22.05.17.
 */

public interface ApiEndPoint {

    AppResponse<ArtistSearchResponse> searchArtist(String query, String index);

    AppResponse<AlbumSearchResponse> searchAlbum(String query);

    AppResponse<TrackResponse> getTracksForAlbum(int id, String index);

}
