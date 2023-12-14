package sample.GUI.Model;

import javafx.collections.FXCollections;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BE.SongsInPlaylist;
import sample.BLL.PlaylistManager;
import javafx.collections.ObservableList;
import sample.BLL.SongManager;
import sample.DAL.DatabaseConnector;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainModel {

    private PlaylistManager playlistManager;
    private SongManager songManager;
    private ObservableList<Playlist> availablePlaylists;
    private ObservableList<Song> availableSongs;
    private List<File> songs;
    private File song;
    private int songNumber;
    private Media media;
    private MediaPlayer mediaPlayer;
    private int tempIndex;

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
            updatePlaylists();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSongFromPlaylist(int entryID, int playlistID) throws SQLException {
        try {
            playlistManager.removeSongFromPlaylist(entryID, playlistID);
            updatePlaylists();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updatePlaylists() {
        try {
            List<Playlist> updatedPlaylists = playlistManager.getAllPlaylists();
            availablePlaylists.setAll(updatedPlaylists);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void refreshSongs() {
        try {
            // Reload songs from the database
            List<Song> allSongs = songManager.readAllSongs();

            availableSongs.setAll(allSongs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Song> searchSongs(String query) throws Exception {
        return songManager.searchSongs(query);
    }

    public void deleteSong(Song selectedSong)throws Exception {
        // Deletes song in DAL
        songManager.deleteSong(selectedSong);
        // removes it from observable list and UI.
        availableSongs.remove(selectedSong);

    }

    public void initializeSongs() throws IOException {
        // Initialize the first song into the list of songs so that it will always play the first song if nothing is selected
        song = songManager.getSong(0);
        songNumber = 0;
        songs = songManager.getSongs();
        // put this info into the MediaPlayer
        media = new Media(song.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public String tempValueForSong(int index, boolean play) throws IOException {
        if (index != tempIndex) {
            tempIndex = index;
            return playPauseSong(index, play);
        }
        else {
            return playPauseSong(songNumber, play);
        }
    }

    public String playPauseSong(int index, boolean play) throws IOException {
        // Checks if the song has to play or just return the name of the song which is being played
        if (play) {
            // If the MediaPlayer is already playing, pause it
            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                mediaPlayer.pause();
            }
            // else play it
            else {
                return playSong(index);
            }
        }
        else {
            return songs.get(songNumber).getName();
        }
        return songs.get(songNumber).getName();
    }

    // This is used to actually play the song updating the MediaPlayer
    public String playSong(int index) throws IOException {
        // by getting in which song to play
        songNumber = index;
        song = songManager.getSong(index);
        // and updating the MediaPlayer
        media = new Media(song.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        // and then return the title of the song to the MainController so that it can be displayed for the user
        return songs.get(songNumber).getName();
    }

    public String previousSong(boolean previous) throws IOException {
        // Checks first if it should return to the previous song in the list or just return the name of the current song for displaying
        if (previous) {
            // After that it stops the MediaPlayer so that it can refresh the MediaPlayer
            mediaPlayer.stop();
            // If the song number is more than 0, decrease the song number by one to go to the previous song
            if (songNumber > 0) {
                songNumber--;
            }
            // Else make the current song the last song in the list so that it loops
            else {
                songNumber = songs.size() - 1;
            }
            return playSong(songNumber);
        }
        else {
            return songs.get(songNumber).getName();
        }
    }

    public String nextSong(boolean skip) throws IOException {
        // Checks first if it should skip to the next song in the list or just return the name of the current song for displaying
        if (skip) {
            // After that it stops the MediaPlayer so that it can refresh the MediaPlayer
            mediaPlayer.stop();
            // If the song less than the size of the song, increase the song number to go the next song
            // The song size starts from 1 because thats how you load files, so we have to -1
            if (songNumber < songs.size() - 1) {
                songNumber++;
            }
            // Else set the song to the first song in the list
            else {
                songNumber = 0;
            }
            return playSong(songNumber);
        }
        else {
            return songs.get(songNumber).getName();
        }
    }

    public void moveSongUp(int playlistID, int currentOrder) throws SQLException{
            playlistManager.moveSongUp(playlistID, currentOrder);
    }

    public void moveSongDown(int playlistID, int currentOrder) throws SQLException{
        playlistManager.moveSongDown(playlistID, currentOrder);
    }

}
