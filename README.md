# Spotify Discover Weekly Retention

![Java](https://img.shields.io/badge/Java-26-orange?logo=openjdk)
![CI](https://github.com/coltonrandall/spotify-discover-weekly-retention-app/actions/workflows/maven.yml/badge.svg)

Automatically saves tracks from your Spotify Discover Weekly playlist into a permanent playlist, so you never lose them when it resets each Monday.

## Prerequisites

- Java 26
- A [Spotify Developer](https://developer.spotify.com/dashboard) app with the following redirect URI: `http://127.0.0.1:8080/login/oauth2/code/spotify`

## Setup

1. Clone the repo
2. Copy `.env.example` to `.env` and fill in your Spotify credentials
3. Add the env vars to your run configuration (see `.env.example`)

## Configuration

The app reads the following environment variables (wired through `application.properties`):

| Variable | Description |
| --- | --- |
| `SPOTIFY_CLIENT_ID` | Client ID from your Spotify Developer app. |
| `SPOTIFY_CLIENT_SECRET` | Client secret from your Spotify Developer app. |
| `TARGET_PLAYLIST_ID` | The Spotify playlist ID to copy Discover Weekly tracks into. Must be a playlist you own. |

The Discover Weekly playlist itself is discovered automatically from the authenticated user's library — no ID is required.

### Spotify scopes

The OAuth flow requests the following scopes:

- `playlist-read-private`
- `playlist-modify-private`
- `playlist-modify-public`
- `user-library-read`

## Running

```bash
./mvnw spring-boot:run
```

Then visit `http://127.0.0.1:8080`.
