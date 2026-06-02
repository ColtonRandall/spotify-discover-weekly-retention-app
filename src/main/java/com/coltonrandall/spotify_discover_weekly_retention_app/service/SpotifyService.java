package com.coltonrandall.spotify_discover_weekly_retention_app.service;

import com.coltonrandall.spotify_discover_weekly_retention_app.model.AddTrackRequest;
import com.coltonrandall.spotify_discover_weekly_retention_app.model.Playlist;
import com.coltonrandall.spotify_discover_weekly_retention_app.model.PlaylistResponse;
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
    private static final String AUTH = "Authorization";
    private static final String PLAYLISTS_ENDPOINT = "/v1/playlists/";
    private static final String BEARER_TOKEN = "Bearer ";


    @Value("${spotify.target-playlist-id}")
    private String targetPlaylistId;

    public SpotifyService() {
        this.client = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public String findDiscoverWeeklyPlaylistId(String accessToken) {
        log.info("Searching user playlists for Discover Weekly...");
        String url = "/v1/me/playlists?limit=50";

        while (url != null) {
            PlaylistResponse response = client.get()
                    .uri(url)
                    .header(AUTH, BEARER_TOKEN + accessToken)
                    .retrieve()
                    .body(PlaylistResponse.class);

            if (response == null) break;

            for (Playlist playlist : response.items()) {
                if ("Discover Weekly".equals(playlist.name()) && "spotify".equals(playlist.owner()
                        .id())) {
                    log.info("Found Discover Weekly playlist: '{}'", playlist.id());
                    return playlist.id();
                }
            }

            String next = response.next();
            url = (next != null) ? next.replace(BASE_URL, "") : null;
        }

        throw new IllegalStateException("Discover Weekly playlist not found in user's library. Make sure it has been added to your Spotify account.");
    }

    public List<String> getDiscoverWeeklyTracks(String accessToken) {
        String discoverWeeklyPlaylistId = findDiscoverWeeklyPlaylistId(accessToken);
        log.info("Fetching tracks for discover weekly playlist ID: '{}'", discoverWeeklyPlaylistId);

        TracksResponse response = client.get()
                .uri(PLAYLISTS_ENDPOINT + discoverWeeklyPlaylistId + "/tracks?additional_types=track")
                .header(AUTH, BEARER_TOKEN + accessToken)
                .retrieve()
                .body(TracksResponse.class);

        if (response == null) return new ArrayList<>();

        return response.items()
                .stream()
                .map(item -> item.track()
                        .uri())
                .toList();
    }

    public void addTracksToPlaylist(List<String> trackUris, String accessToken) {
        log.info("Adding tracks for target playlist ID: '{}'", targetPlaylistId);

        client.post()
                .uri(PLAYLISTS_ENDPOINT + targetPlaylistId + "/tracks")
                .header(AUTH, BEARER_TOKEN + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AddTrackRequest(trackUris))
                .retrieve()
                .toBodilessEntity();
    }

    public List<String> getTargetPlaylistTrackUris(String accessToken) {
        log.info("Fetching tracks for target playlist ID: '{}'", targetPlaylistId);
        String url = PLAYLISTS_ENDPOINT + targetPlaylistId + "/tracks?additional_types=track";
        List<String> uris = new ArrayList<>();

        while (url != null) {
            TracksResponse response = client.get()
                    .uri(url)
                    .header(AUTH, BEARER_TOKEN + accessToken)
                    .retrieve()
                    .body(TracksResponse.class);

            if (response == null) break;

            response.items()
                    .stream()
                    .map(item -> item.track()
                            .uri())
                    .forEach(uris::add);

            String next = response.next();
            url = (next != null) ? next.replace(BASE_URL, "") : null;
        }

        return uris;
    }
}
