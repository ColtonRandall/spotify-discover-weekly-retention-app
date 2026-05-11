package com.coltonrandall.spotify_discover_weekly_retention_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TracksResponse(List<TrackItem> items, String next) {
}
