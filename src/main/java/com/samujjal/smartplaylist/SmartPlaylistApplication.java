package com.samujjal.smartplaylist;

import com.samujjal.smartplaylist.client.MusixMatchClient;
import lombok.extern.slf4j.Slf4j;
import org.jmusixmatch.entity.track.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
@Slf4j
public class SmartPlaylistApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartPlaylistApplication.class, args);
	}

//	@Component
//	class SamRunner implements CommandLineRunner {
//
//		@Autowired
//		private MusixMatchClient musixMatchClient;
//		@Override
//		public void run(String... args) throws Exception {
//			List<Track> trackList = musixMatchClient.searchTracks("love");
//			log.info(musixMatchClient.getLyrics("68853007"));
//		}
//	}

}
