package com.samujjal.smartplaylist.controller;

import com.samujjal.smartplaylist.exception.ServiceError;
import com.samujjal.smartplaylist.model.Song;
import com.samujjal.smartplaylist.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/api/v1/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
    @Operation(summary = "Create playlist for search string.",
            description =
                    "Returns a song by creating new playlist based on the search string")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Create Playlist instance for search String and return first song",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "503",
                    description = "Playlist service encountered a temporary internal error",
                    content = @Content(schema = @Schema(implementation = ServiceError.class)))})
    @PostMapping
    public ResponseEntity<Song> createPlaylist(
            @Parameter(description = "Search String", example = "Pots and Pans")
            @RequestParam("searchString") @NotBlank String searchString) {
        log.info("Creating playlist for search string {}", searchString);
        return ResponseEntity.ok(playlistService.createPlaylist(searchString));
    }

    @Operation(summary = "Play next song in a playlist given current track ID",
            description =
                    "Returns a track by using the lyrics in the current song for a given playlist id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Next unplayed track found based on the lyrics of current track in playlist",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "503",
                    description = "Playlist service encountered a temporary internal error",
                    content = @Content(schema = @Schema(implementation = ServiceError.class)))})
    @PutMapping("/{playlistId}/next")
    public ResponseEntity<Song> playNextSong(
            @PathVariable("playlistId") String playlistId,
            @Parameter(description = "Track ID", example = "35254943")
            @RequestParam("trackId") Integer trackId) {
        log.info("Looking for next song to {} in playlist {}", trackId, playlistId);
        return ResponseEntity.ok().body(playlistService.playNextSong(playlistId, trackId));
    }

}
