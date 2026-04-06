package com.coltonrandall.spotify_discover_weekly_retention_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TrackItem(Track track) {
}
