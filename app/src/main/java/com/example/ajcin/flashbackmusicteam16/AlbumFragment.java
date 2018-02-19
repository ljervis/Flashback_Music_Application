package com.example.ajcin.flashbackmusicteam16;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

/** AlbumFragment class to handle actions from the album list.
  * Author: CSE 110 - Team 16, Winter 2018
  * Date: February 7, 2018
 */
public class AlbumFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    String[] album_list_string;
    Album selected_album;

    /** Required empty constructor */
    public AlbumFragment() {}

    /** Create the AlbumFragment */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        album_list_string = bundle.getStringArray("albums");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, album_list_string);
        setListAdapter(adapter);

        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_album, container, false);
         view.findViewById(android.R.id.list).setScrollContainer(true);
         return view;
    }

    /** onListItemClick
      * When album is clicked, display popup to view album or play whole album.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        selected_album = ((AlbumView)getActivity()).populateMusic.getAlbum(album_list_string[position]);
        String[] album_song_list = ((AlbumView)getActivity()).populateMusic.getSongListInAlbumString(selected_album);
        Bundle album_song_bundle = new Bundle();
        album_song_bundle.putStringArray("album_songs",album_song_list);
        final albumsongsFragment album_songs_fragment = new albumsongsFragment();
        album_songs_fragment.setArguments(album_song_bundle);

        //Show pop up menu
        PopupMenu popup = new PopupMenu(getContext(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.one){
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.musicItems,album_songs_fragment).commit();
                }
                else if(item.getItemId()==R.id.two){
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_name", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if(((AlbumView)getActivity()).mediaPlayer == null){
                        ((AlbumView)getActivity()).createMediaPlayer();
                    }
                    ((AlbumView)getActivity()).mediaPlayer.reset();
                    ((AlbumView)getActivity()).album_playlist  = new ArrayList<Song>(selected_album.get_album_songs());
                    Song nowPlaying = ((AlbumView)getActivity()).album_playlist.get(0);

                    editor.putString("song_name", nowPlaying.get_title());
                    editor.putString("artist_name", nowPlaying.get_artist());
                    editor.putString("album_name", nowPlaying.get_album());
                    editor.putString("address", nowPlaying.get_last_played_address());
                    editor.putString("time", nowPlaying.get_last_time());
                    editor.apply();

                    changeToNowPlaying(nowPlaying);
                    ((AlbumView)getActivity()).nextAlbumTrack();
                }
              
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    /** changeToNowPlaying
     * Change the view to Now Playing with the selected song's information.
     * @param song current Song being played
     */
    public void changeToNowPlaying(Song song) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle song_bundle = new Bundle();
        String[] song_name = new String[1];
        song_name[0] = song.get_title();
        song_bundle.putStringArray("song", song_name);
        NowPlayingFragment npFragment = new NowPlayingFragment();
        npFragment.setArguments(song_bundle);
        ((AlbumView)getActivity()).navigation.getMenu().getItem(2).setChecked(true);
        transaction.replace(R.id.musicItems, npFragment).commit();
    }

    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
                   // + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
