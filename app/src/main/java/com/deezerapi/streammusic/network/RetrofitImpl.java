package com.deezerapi.streammusic.network;

import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.AppException;
import com.deezerapi.streammusic.api.ApiEndPoint;
import com.deezerapi.streammusic.model.AlbumSearchResponse;
import com.deezerapi.streammusic.model.ArtistSearchResponse;
import com.deezerapi.streammusic.model.TrackResponse;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by nishant on 22.05.17.
 */

public class RetrofitImpl implements ApiEndPoint {

    @Override
    public AppResponse<AlbumSearchResponse> searchAlbum(String query) {
        try {
            Response<AlbumSearchResponse> response = App.getApiService().searchAlbum(query).execute();
            return new AppResponse<AlbumSearchResponse>(response.isSuccessful(), response.body(), new AppException(response.code()));
        } catch (Exception ex) {
            return new AppResponse<AlbumSearchResponse>(false, null, new AppException(ex.getMessage()));
        }
    }

    @Override
    public AppResponse<ArtistSearchResponse> searchArtist(String query, String index) {
        try {
            Response<ArtistSearchResponse> response = App.getApiService().searchArtist(query, index).execute();
            return new AppResponse<ArtistSearchResponse>(response.isSuccessful(), response.body(), new AppException(response.code()));
        } catch (Exception ex) {
            return new AppResponse<ArtistSearchResponse>(false, null, new AppException(ex.getMessage()));
        }
    }

    @Override
    public AppResponse<TrackResponse> getTracksForAlbum(int id, String index) {
        try {
            Response<TrackResponse> response = App.getApiService().getTracksForAlbum(id, index).execute();
            return new AppResponse<TrackResponse>(response.isSuccessful(), response.body(), new AppException(response.code()));
        } catch (Exception ex) {
            return new AppResponse<TrackResponse>(false, null, new AppException(ex.getMessage()));
        }
    }

}
