package com.coltonrandall.spotify_discover_weekly_retention_app.service;

import com.coltonrandall.spotify_discover_weekly_retention_app.model.AddTrackRequest;
import com.coltonrandall.spotify_discover_weekly_retention_app.model.TracksResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyService {

    private static final Logger log = LoggerFactory.getLogger(SpotifyService.class);
    RestClient client;
    private static final String BASE_URL = "https://api.spotify.com";

    @Value("${spotify.discover-weekly-playlist-id}")
    private String discoverWeeklyPlaylistId;

    @Value("${spotify.target-playlist-id}")
    private String targetPlaylistId;

    public SpotifyService() {
        this.client = RestClient
                .builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public List<String> getDiscoverWeeklyTracks(String accessToken) {
        log.info("Fetching tracks for discover weekly playlist ID: '{}'", discoverWeeklyPlaylistId);

        TracksResponse response = client.get()
                .uri("/v1/playlists/" + discoverWeeklyPlaylistId + "/tracks?additional_types=track")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(TracksResponse.class);

        if (response == null) return new ArrayList<>();

        return response.items().stream()
                .map(item -> item.track().uri())
                .toList();
    }

    public void addTracksToPlaylist(List<String> trackUris, String accessToken) {
        log.info("Adding tracks for target playlist ID: '{}'", targetPlaylistId);

        client.post()
                .uri("/v1/playlists/" + targetPlaylistId + "/tracks")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AddTrackRequest(trackUris))
                .retrieve()
                .toBodilessEntity();
    }
}
