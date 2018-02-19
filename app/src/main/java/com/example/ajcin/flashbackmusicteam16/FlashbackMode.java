package com.example.ajcin.flashbackmusicteam16;

import android.location.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sqin8 on 2/17/2018.
 */

public class FlashbackMode {
    Location currentLocation;
    LocalDateTime localDateTime;
    List<Song> songs;
    PopulateMusic populateMusic;
    public FlashbackMode(Location currentLocation, LocalDateTime localDateTime,PopulateMusic populateMusic)
    {
        this.populateMusic=populateMusic;

        this.currentLocation=currentLocation;
        this.localDateTime=localDateTime;
    }
    public ArrayList<Song> initiate()
    {
        songs=new ArrayList<Song>();
        songs=populateMusic.getSongList();
        this.readyScores();
        return (ArrayList)this.songs;

    }
    public void readyScores()
    {
        for(Song s: songs) {
            int dateTimeScore = Integer.MAX_VALUE;
            int locationScore = Integer.MAX_VALUE;
            for (LocalDateTime p : s.getListOfLocalDateTimes()) {
                int tmp = Math.abs(p.getHour() - localDateTime.getHour());
                if (dateTimeScore < tmp) {
                    dateTimeScore = tmp;
                }
            }
            for (Location p : s.getListOfLocations()) {
                int tmp = (LocationInfo.getDistance(p, currentLocation));
                if (locationScore < tmp) {
                    locationScore = tmp;
                }
            }
            s.setScore((int) (locationScore / 100) + (int) (dateTimeScore * 2));
        }

        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getScore()-t1.getScore();
            }
        });
    }


}
