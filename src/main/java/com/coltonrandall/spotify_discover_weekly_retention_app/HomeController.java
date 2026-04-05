package com.coltonrandall.spotify_discover_weekly_retention_app;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User user) {
        String spotifyId = user.getAttribute("id");
        String displayName = user.getAttribute("display_name");
        return "Logged in as: " + displayName + " (" + spotifyId + ")";
    }
}
