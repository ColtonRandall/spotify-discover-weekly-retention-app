package com.coltonrandall.spotify_discover_weekly_retention_app.controller;

import com.coltonrandall.spotify_discover_weekly_retention_app.service.SpotifyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    SpotifyService spotifyService;
    OAuth2AuthorizedClientService authorizedClientService;

    public HomeController(SpotifyService spotifyService, OAuth2AuthorizedClientService authorizedClientService) {
        this.spotifyService = spotifyService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/")
    public List<String> home(@AuthenticationPrincipal OAuth2User user) {

        OAuth2AuthorizedClient oAuthClient = authorizedClientService.loadAuthorizedClient("spotify", user.getName());
        String accessToken = oAuthClient.getAccessToken().getTokenValue();

        return spotifyService.getDiscoverWeeklyTracks(accessToken);
    }
}
