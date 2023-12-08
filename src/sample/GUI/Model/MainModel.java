package sample.GUI.Model;

import javafx.collections.FXCollections;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BE.SongsInPlaylist;
import sample.BLL.PlaylistManager;
import javafx.collections.ObservableList;
import sample.BLL.SongManager;
import sample.DAL.DatabaseConnector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainModel {

    private PlaylistManager playlistManager;
    private SongManager songManager;
    private ObservableList<Playlist> availablePlaylists;
    private ObservableList<Song> availableSongs;
    private List<File> songs;
    private int songNumber;
    private File directory;
    private File[] files;
    private Media media;
    private MediaPlayer mediaPlayer;

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

    public Playlist createPlaylist(String name) throws Exception {
        return playlistManager.createPlaylist(name);
    }

    public void editPlaylist(Playlist selectedPlaylist){
        try {
            playlistManager.editPlaylist(selectedPlaylist);
            availablePlaylists.set(availablePlaylists.indexOf(selectedPlaylist), selectedPlaylist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePlaylist(Playlist playlist) {
        try {
            playlistManager.deletePlaylist(playlist);
            availablePlaylists.remove(playlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSongToPlaylist(int playlistID, int songID) {
        try {
            playlistManager.addSongToPlaylist(playlistID, songID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSongFromPlaylist(int playlistID, int songID) {
        try {
            playlistManager.removeSongFromPlaylist(playlistID, songID);
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public void initializeSongs() throws IOException {
        songs = songManager.getSongs();

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public String playSong() {
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
            return songs.get(songNumber).getName();
        }
        return songs.get(songNumber).getName();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public String previousSong() {
        if (songNumber > 0) {
            songNumber--;
        } else {
            songNumber = songs.size() - 1;
        }
        return resetMediaPlayer();
    }

    public String nextSong() {
        if (songNumber < songs.size() - 1) {
            songNumber++;
        } else {
            songNumber = 0;
        }
        return resetMediaPlayer();
    }

    public String resetMediaPlayer() {
        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        return songs.get(songNumber).getName();
    }
}
