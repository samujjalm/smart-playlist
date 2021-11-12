package com.samujjal.smartplaylist.client;

import com.samujjal.smartplaylist.exception.ErrorCode;
import com.samujjal.smartplaylist.exception.PlaylistServiceException;
import lombok.extern.slf4j.Slf4j;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MusixMatchClient {

    private MusixMatch musixMatch;
    private int pageSize;

    public MusixMatchClient(@Value("${musixmatch.apikey}") String apiKey, @Value("${musixmatch.page-size}") int pageSize) {
        this.musixMatch = new MusixMatch(apiKey);
        this.pageSize = pageSize;
    }

    public String getLyrics(String trackId) {
        try {
            Lyrics lyrics = musixMatch.getLyrics(Integer.parseInt(trackId));
            return lyrics.getLyricsBody();
        } catch (Exception ex) {
            log.error("Error fetching lyrics for {}", trackId, ex);
            throw new PlaylistServiceException(ErrorCode.LYRICS_NOT_FOUND, ex, ex.getMessage());
        }
    }

    public List<Track> searchTracks(String queryByLyrics){
        try {
            return musixMatch.searchTracks("", "", queryByLyrics, 1, pageSize, true);
        } catch (Exception ex) {
            log.error("Error searching songs for lyrics containing {}", queryByLyrics, ex);
            throw new PlaylistServiceException(ErrorCode.NO_MATCHING_SONGS, ex, ex.getMessage());
        }
    }
}
