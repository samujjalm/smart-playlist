package com.samujjal.smartplaylist.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Song {
    Integer trackId;
    String title;
    String artist;
    String lyrics;
    String playListId;
}
