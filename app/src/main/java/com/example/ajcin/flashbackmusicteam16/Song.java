package com.example.ajcin.flashbackmusicteam16;

/**
 * Created by luke on 2/7/2018.
 */

public class Song {

    private final String song_title;
    private final String song_artist;
    private final String song_album;
    private final long song_id;

    private String last_location;
    private String last_day;
    private String last_time;
    private boolean is_liked;
    private boolean is_favorited;

    public Song(String title,String artist, String album, long id){
        song_title = title;
        song_album = album;
        song_artist = artist;
        song_id = id;
    }

    public String get_title(){return song_title;}
    public String get_album(){return song_album;}
    public String get_artist(){return song_artist;}
    public String get_last_location(){return last_location;}
    public String get_last_day(){return last_day;}
    public String get_last_time(){return last_time;}
    public Boolean check_liked(){return is_liked;}
    public Boolean check_favorited(){return is_favorited;}
    public void update_last_location(String location){last_location = location;}
    public void update_last_day(String day){last_day = day;}
    public void update_last_time(String time){last_time = time;}
}
