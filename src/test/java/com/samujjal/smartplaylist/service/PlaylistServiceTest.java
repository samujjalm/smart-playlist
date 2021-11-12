package com.samujjal.smartplaylist.service;

import com.samujjal.smartplaylist.client.MusixMatchClient;
import com.samujjal.smartplaylist.model.Song;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @InjectMocks
    PlaylistService playlistService = new PlaylistService();
    @Mock
    private MusixMatchClient musixMatchClient;
    @Test
    public void testCreatePlaylist(){
        Track track = new Track();
        TrackData trackData = new TrackData();
        trackData.setTrackId(123);
        trackData.setArtistName("artist_name");
        trackData.setTrackName("track_name");
        track.setTrack(trackData);
        when(musixMatchClient.searchTracks("one love")).thenReturn(Collections.singletonList(track));
        when(musixMatchClient.getLyrics("123")).thenReturn("abc");
        Song oneLove = playlistService.createPlaylist("one love");
        assertEquals(123, oneLove.getTrackId());
        assertEquals("artist_name", oneLove.getArtist());
        assertEquals("track_name", oneLove.getTitle());
        assertEquals("abc", oneLove.getLyrics());

    }
}
