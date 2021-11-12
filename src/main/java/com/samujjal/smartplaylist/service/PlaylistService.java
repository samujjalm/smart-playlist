package com.samujjal.smartplaylist.service;

import com.samujjal.smartplaylist.client.MusixMatchClient;
import com.samujjal.smartplaylist.exception.ErrorCode;
import com.samujjal.smartplaylist.exception.PlaylistServiceException;
import com.samujjal.smartplaylist.model.Song;
import com.samujjal.smartplaylist.util.PlaylistUtil;
import org.apache.commons.lang3.StringUtils;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class PlaylistService {
    @Autowired
    private MusixMatchClient musixMatchClient;

    @Value("${musixmatch.words-in-query}")
    private int wordsInQuery;
    private Map<String, Set<Integer>> playlistPlayedTrackMap = new HashMap<>();
    private Map<Integer, String> trackIdLyricsCache = new HashMap<>();

    public Song createPlaylist(String lyricsSearchQuery){
        List<Track> trackList = searchTracks(lyricsSearchQuery);
        return getSong(UUID.randomUUID().toString(), new HashSet<>(), trackList.get(0));
    }

    public Song playNextSong(String playlistId, Integer trackId){
        if(playlistPlayedTrackMap.get(playlistId) == null){
            throw new PlaylistServiceException(ErrorCode.PLAYLIST_NOT_FOUND, null, "no playlist found");
        }
        String trackLyrics = fetchLyricsForTrackId(trackId);

        if(StringUtils.isBlank(trackLyrics)){
            throw new PlaylistServiceException(ErrorCode.LYRICS_NOT_FOUND, null, "no lyrics found");
        }

        String nextSongSearchQuery = PlaylistUtil.generateRandomQuery(trackLyrics, wordsInQuery);
        List<Track> trackList = searchTracks(nextSongSearchQuery);
        Set<Integer> playedTrackIdSet = playlistPlayedTrackMap.get(playlistId);
        for (Track track : trackList) {
            if (!playedTrackIdSet.contains(track.getTrack().getTrackId())) {
                return getSong(playlistId, playedTrackIdSet, track);
            }
        }
        throw new PlaylistServiceException(ErrorCode.NO_MATCHING_SONGS, null, "no songs found");
    }

    private String fetchLyricsForTrackId(Integer trackId) {
        String trackLyrics = trackIdLyricsCache.get(trackId);
        if(trackLyrics == null){
            trackLyrics = musixMatchClient.getLyrics(trackId.toString());
        }
        return trackLyrics;
    }

    private Song getSong(String playlistId, Set<Integer> playedTrackIdSet, Track track) {
        playedTrackIdSet.add(track.getTrack().getTrackId());
        String lyrics = musixMatchClient.getLyrics(track.getTrack().getTrackId().toString());
        trackIdLyricsCache.put(track.getTrack().getTrackId(), lyrics);
        playlistPlayedTrackMap.put(playlistId, playedTrackIdSet);
        return Song.builder()
                .trackId(track.getTrack().getTrackId())
                .artist(track.getTrack().getArtistName())
                .playListId(playlistId)
                .title(track.getTrack().getTrackName())
                .lyrics(lyrics)
                .build();
    }

    private List<Track> searchTracks(String nextSongSearchQuery) {
        List<Track> trackList = musixMatchClient.searchTracks(nextSongSearchQuery);
        if(trackList.isEmpty()) {
            throw new PlaylistServiceException(ErrorCode.NO_MATCHING_SONGS, null, "no songs found");
        }

        return trackList;
    }


}
