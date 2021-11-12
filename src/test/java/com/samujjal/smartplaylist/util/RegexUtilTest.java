package com.samujjal.smartplaylist.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegexUtilTest {
    @Test
    public void testNextLyricsQueryExtraction(){
        String lyrics = "Love is in the air\nEverywhere I look around\nLove is in the air\nEvery sight and every sound\nAnd I don't know if I'm being foolish\nDon't know if I'm being wise\nBut it's something that I must believe in\nAnd it's there when I look in your eyes\nLove is in the air\nIn the whisper of the trees\nLove is in the air\nIn the thunder of the sea\n...\n\n******* This Lyrics is NOT for Commercial use *******\n(1409622230991)";
        String randomQuery = PlaylistUtil.generateRandomQuery(lyrics, 5);
        assertNotNull(randomQuery);
    }
}
