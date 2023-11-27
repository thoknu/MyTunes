package sample.GUI.Model;

import javafx.collections.FXCollections;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BLL.PlaylistManager;
import javafx.collections.ObservableList;
import sample.BLL.SongManager;

import java.util.List;

public class MainModel {
    private PlaylistManager playlistManager;
    private SongManager songManager;
    private ObservableList<Playlist> availablePlaylists;
    private ObservableList<Song> availableSongs;

    public MainModel() throws Exception {
        playlistManager = new PlaylistManager();
        songManager = new SongManager();
        availablePlaylists = FXCollections.observableArrayList();
        availablePlaylists.addAll(playlistManager.getAllPlaylists());
        availableSongs = FXCollections.observableArrayList();
        availableSongs.addAll(songManager.getAllSongs());
    }
    public ObservableList<Playlist> getObservablePlaylists() {
        return availablePlaylists;
    }

    public ObservableList<Song> getObservableSongs() {
        return availableSongs;
    }


    public List<Song> getAllSongsInPlaylist(Playlist playlist) throws Exception {
        return playlistManager.getAllSongsInPlaylist(playlist);
    }

    public Song getSong(Playlist playlist, Song song) throws Exception {
        return playlistManager.getSong(playlist, song);
    }
}
