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
import sample.DAL.PlaylistDAO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainModel {

    private PlaylistManager playlistManager;
    private SongManager songManager;
    private PlaylistDAO playlistDAO;
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
        playlistDAO = new PlaylistDAO();
        availablePlaylists = FXCollections.observableArrayList(playlistManager.getAllPlaylists());
        availableSongs = FXCollections.observableArrayList(songManager.readAllSongs());
    }

    ////////////////////////////
    //// Playlist Functions ////
    ////////////////////////////

    public ObservableList<Playlist> getObservablePlaylists() {
        return availablePlaylists;
    }

    public ObservableList<Song> getObservableSongs() {
        return availableSongs;
    }


    public List<SongsInPlaylist> getAllSongsInPlaylist(Playlist playlist) throws Exception {
        return playlistManager.getAllSongsInPlaylist(playlist);
    }

    public Playlist getPlaylistByID(int playlistID){
        for (Playlist playlist : availablePlaylists){
            if (playlist.getId() == playlistID){
                return playlist;
            }
        }
        return null;
    }

    public Song getSongByID(int songID) throws SQLException {
        return songManager.getSongByID(songID);
    }

    /*public Song getSong(Playlist playlist, Song song) throws Exception {
        return playlistManager.getSong(playlist, song);
    }*/

    public Playlist createPlaylist(String name) throws SQLException {
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

    public void moveSongUp(int playlistID, int currentOrder) throws SQLException{
        playlistDAO.moveSongUp(playlistID, currentOrder);
    }

    public void moveSongDown(int playlistID, int currentOrder) throws SQLException{
        playlistDAO.moveSongDown(playlistID, currentOrder);
    }

    ////////////////////////
    //// Song Functions ////
    ////////////////////////

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
        //refresh playlists to update their info
        refreshPlaylists();
        // removes it from observable list and UI.
        availableSongs.remove(selectedSong);
    }

    private void refreshPlaylists() {
        // Fetch the latest playlist data and update availablePlaylists
        try {
            List<Playlist> updatedPlaylists = playlistManager.getAllPlaylists();
            availablePlaylists.setAll(updatedPlaylists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    ////////////////////////////////
    //// Media-player Functions ////
    ////////////////////////////////


    // Method to get the next song
    public Song getNextSong(int playlistId, int currentSongId) throws Exception {
        List<SongsInPlaylist> songsInPlaylist = playlistManager.getAllSongsInPlaylist(getPlaylistByID(playlistId));
        for (int i = 0; i < songsInPlaylist.size(); i++) {
            if (songsInPlaylist.get(i).getSongID() == currentSongId) {
                // Check if it's not the last song
                if (i < songsInPlaylist.size() - 1) {
                    return songManager.getSongByID(songsInPlaylist.get(i + 1).getSongID());
                }
                break;
            }
        }
        return null;
    }

    // Method to get the previous song
    public Song getPreviousSong(int playlistId, int currentSongId) throws Exception {
        List<SongsInPlaylist> songsInPlaylist = playlistManager.getAllSongsInPlaylist(getPlaylistByID(playlistId));
        for (int i = 0; i < songsInPlaylist.size(); i++) {
            if (songsInPlaylist.get(i).getSongID() == currentSongId) {
                // Check if it's not the first song
                if (i > 0) {
                    return songManager.getSongByID(songsInPlaylist.get(i - 1).getSongID());
                }
                break;
            }
        }
        return null;
    }



}
