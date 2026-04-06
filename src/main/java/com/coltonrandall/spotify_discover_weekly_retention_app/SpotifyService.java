package com.coltonrandall.spotify_discover_weekly_retention_app;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SpotifyService {

    RestClient client;
    private static final String BASE_URL = "https://api.spotify.com";
    private static final String PLAYLISTS_ENDPOINT = "/v1/me/playlists";

    public SpotifyService() {
        this.client = RestClient
                .builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public String getPlaylists(String accessToken){
        return client.get()
                .uri(PLAYLISTS_ENDPOINT)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }
}
