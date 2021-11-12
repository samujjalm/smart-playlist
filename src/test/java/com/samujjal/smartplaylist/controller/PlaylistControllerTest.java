package com.samujjal.smartplaylist.controller;

import com.samujjal.smartplaylist.exception.ErrorCode;
import com.samujjal.smartplaylist.exception.PlaylistServiceException;
import com.samujjal.smartplaylist.model.Song;
import com.samujjal.smartplaylist.service.PlaylistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaylistController.class)
class PlaylistControllerTest {
    private static final String API_BASE_PATH_V1 = "/api/v1/playlists";
    @MockBean
    private PlaylistService playlistService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Successful Playlist creation with a song in response")
    void testPlaylistCreation() throws Exception {
        String lyricsSearchQuery = "new love";
        when(playlistService.createPlaylist(lyricsSearchQuery)).thenReturn(Song.builder()
                        .trackId(1)
                        .title("abc")
                        .artist("def")
                        .lyrics("xyz")
                        .playListId("cab347f6-8642-4bd9-a9ac-c67986ba81c8")
                .build());
        mockMvc.perform(post(API_BASE_PATH_V1).param("searchString", lyricsSearchQuery).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(
                        content().json("{\"trackId\":1,\"title\":\"abc\",\"artist\":\"def\",\"lyrics\":\"xyz\",\"playListId\":\"cab347f6-8642-4bd9-a9ac-c67986ba81c8\"}"));
    }

    @Test
    @DisplayName("Create Playlist: no songs with such lyrics")
    void testPlaylistCreationFailureInvalidSearchQuery() throws Exception {
        String lyricsSearchQuery = "no love";
        when(playlistService.createPlaylist(lyricsSearchQuery))
                .thenThrow(new PlaylistServiceException(ErrorCode.NO_MATCHING_SONGS, "", ""));
        mockMvc.perform(post(API_BASE_PATH_V1).param("searchString", lyricsSearchQuery).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(
                        content().json("{\n" +
                                "    \"errorMessage\": \"\",\n" +
                                "    \"errorCode\": \"NO_MATCHING_SONGS\",\n" +
                                "    \"details\": \"\"\n" +
                                "}"));
    }

    @Test
    @DisplayName("Successful play of next song in the playlist")
    void testPlayNextSongSuccess() throws Exception {
        String playlistId = "cab347f6-8642-4bd9-a9ac-c67986ba81c8";
        int trackId = 1254;
        when(playlistService.playNextSong(playlistId, trackId)).thenReturn(Song.builder()
                .trackId(1)
                .title("abc")
                .artist("def")
                .lyrics("xyz")
                .playListId(playlistId)
                .build());
        mockMvc.perform(put(API_BASE_PATH_V1 + "/{playlistId}/next", playlistId).param("trackId", String.valueOf(trackId)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(
                        content().json("{\"trackId\":1,\"title\":\"abc\",\"artist\":\"def\",\"lyrics\":\"xyz\",\"playListId\":\"cab347f6-8642-4bd9-a9ac-c67986ba81c8\"}"));
    }

    @Test
    @DisplayName("Unsuccessful play of next song in the playlist")
    void testPlayNextSongFailure() throws Exception {
        String playlistId = "cab347f6-8642-4bd9-a9ac-c67986ba81c8";
        int trackId = 1254;
        when(playlistService.playNextSong(playlistId, trackId))
                .thenThrow(new PlaylistServiceException(ErrorCode.LYRICS_NOT_FOUND, "", ""));
        mockMvc.perform(put(API_BASE_PATH_V1 + "/{playlistId}/next", playlistId).param("trackId", String.valueOf(trackId)).contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(
                        content().json("{\n" +
                                "    \"errorMessage\": \"\",\n" +
                                "    \"errorCode\": \"LYRICS_NOT_FOUND\",\n" +
                                "    \"details\": \"\"\n" +
                                "}"));
    }
}
