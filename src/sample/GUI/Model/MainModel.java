package sample.GUI.Model;

import javafx.collections.FXCollections;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BE.SongsInPlaylist;
import sample.BLL.PlaylistManager;
import javafx.collections.ObservableList;
import sample.BLL.SongManager;
import sample.DAL.DatabaseConnector;

import java.util.List;

public class MainModel {

    private PlaylistManager playlistManager;
    private SongManager songManager;
    private ObservableList<Playlist> availablePlaylists;
    private ObservableList<Song> availableSongs;

    public MainModel() throws Exception {
        playlistManager = new PlaylistManager();
        songManager = new SongManager();
        availablePlaylists = FXCollections.observableArrayList(playlistManager.getAllPlaylists());
        availableSongs = FXCollections.observableArrayList(songManager.readAllSongs());
    }

    public ObservableList<Playlist> getObservablePlaylists() {
        return availablePlaylists;
    }

    public ObservableList<Song> getObservableSongs() {
        return availableSongs;
    }


    public List<SongsInPlaylist> getAllSongsInPlaylist(Playlist playlist) throws Exception {
        return playlistManager.getAllSongsInPlaylist(playlist);

    }

    /*public Song getSong(Playlist playlist, Song song) throws Exception {
        return playlistManager.getSong(playlist, song);
    }*/

    public Playlist createPlaylist(String name) {
        try {
            return playlistManager.createPlaylist(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePlaylist(Playlist playlist) {
        try {
            playlistManager.deletePlaylist(playlist);
            // Remove the playlist from the observable list
            availablePlaylists.remove(playlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSongToPlaylist(int playlistID, int songID) {
        try {
            playlistManager.addSongToPlaylist(playlistID, songID);
            // You may need to update the UI or perform other actions here.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSongFromPlaylist(int playlistID, int songID) {
        try {
            playlistManager.removeSongFromPlaylist(playlistID, songID);
            // You may need to update the UI or perform other actions here.
        } catch (Exception e) {
                e.printStackTrace();
        }
    }





}
