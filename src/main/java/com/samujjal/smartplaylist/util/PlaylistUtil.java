package com.samujjal.smartplaylist.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PlaylistUtil {
    public static String generateRandomQuery(String trackLyrics, int wordsInQuery) {
        if(trackLyrics != null){
            trackLyrics = trackLyrics.replaceAll("\\*", "")
                    .replaceFirst("This Lyrics is NOT for Commercial use", "")
                    .replaceAll("\\d", "")
                    .replaceAll("\\(", "")
                    .replaceAll("\\)", "")
                    .trim();
            String[] cleansedLyrics = trackLyrics.split("\\s|\n");
            String[] randomWords = pickRandomWords(cleansedLyrics, wordsInQuery);
            return String.join(" ", randomWords);
        }
        return null;
    }

    private static String[] pickRandomWords(String[] cleansedLyrics, int wordsInQuery) {
        if(cleansedLyrics.length <= wordsInQuery) return cleansedLyrics;
        Set<Integer> wordIndexSelected = new HashSet<>();
        String[] chosenWords = new String[wordsInQuery];
        int counter = 0;
        while (counter < wordsInQuery){
            int randomNum = ThreadLocalRandom.current().nextInt(0, cleansedLyrics.length);
            if(!wordIndexSelected.contains(randomNum)){
                chosenWords[counter] = cleansedLyrics[randomNum];
                wordIndexSelected.add(randomNum);
                counter++;
            }
        }
        return chosenWords;
    }
}
