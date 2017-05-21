package com.deezerapi.streammusic.api;

import com.deezerapi.streammusic.model.AlbumSearchResponse;
import com.deezerapi.streammusic.model.ArtistSearchResponse;
import com.deezerapi.streammusic.model.TrackResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nishant on 17.05.17.
 */

public interface ApiService {

    @GET("search/artist")
    Call<ArtistSearchResponse> searchArtist(@Query("q") String query, @Query("index") String index );

    @GET("search/album")
    Call<AlbumSearchResponse> searchAlbum(@Query("q") String query);

    @GET("album/{id}/tracks")
    Call<TrackResponse> getTracksForAlbum(@Path("id") int id,@Query("index") String index);

}
