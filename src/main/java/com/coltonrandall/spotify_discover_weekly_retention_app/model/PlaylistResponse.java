package com.coltonrandall.spotify_discover_weekly_retention_app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlaylistResponse(List<Playlist> items, String next) {
}
