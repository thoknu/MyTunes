package sample.GUI.Model;

import javafx.collections.FXCollections;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BE.SongsInPlaylist;
import sample.BLL.PlaylistManager;
import javafx.collections.ObservableList;
import sample.BLL.SongManager;
import java.sql.SQLException;
import java.util.List;

public class MainModel {

    private PlaylistManager playlistManager;
    private SongManager songManager;
    private ObservableList<Playlist> availablePlaylists;
    private ObservableList<Song> availableSongs;

    /**
     * Creates PlaylistManager and SongManager objects. Gets the current items in the FXML lists.
     *
     * @throws Exception if it cannot create the objects or the items cannot be fetched.
     */
    public MainModel() throws Exception {
        playlistManager = new PlaylistManager();
        songManager = new SongManager();
        availablePlaylists = FXCollections.observableArrayList(playlistManager.getAllPlaylists());
        availableSongs = FXCollections.observableArrayList(songManager.readAllSongs());
    }

    ////////////////////////////
    //// Playlist Functions ////
    ////////////////////////////

    /**
     * Allows fetching of the playlists in the Tableview.
     *
     * @return ObservableList of playlists.
     */
    public ObservableList<Playlist> getObservablePlaylists() {
        return availablePlaylists;
    }

    /**
     * Allows fetching of the songs in the Listview.
     *
     * @return ObservableList of songs.
     */
    public ObservableList<Song> getObservableSongs() {
        return availableSongs;
    }

    /**
     * Gets the songs in the selected playlist.
     *
     * @param playlist The selected playlist.
     * @return The songs in the playlist.
     * @throws Exception If the songs cannot be read.
     */
    public List<SongsInPlaylist> getAllSongsInPlaylist(Playlist playlist) throws Exception {
        return playlistManager.getAllSongsInPlaylist(playlist);
    }

    /**
     * Gets the playlist by its ID.
     *
     * @param playlistID ID of playlist that is wanted.
     * @return The playlist with the ID.
     */
    public Playlist getPlaylistByID(int playlistID){
        for (Playlist playlist : availablePlaylists){
            if (playlist.getId() == playlistID){
                return playlist;
            }
        }
        return null;
    }

    /**
     * Gets the song by its ID.
     *
     * @param songID ID of song that is wanted.
     * @return The song with the ID.
     * @throws SQLException If there is no song with that ID.
     */
    public Song getSongByID(int songID) throws SQLException {
        return songManager.getSongByID(songID);
    }

    /**
     * Deletes a playlist.
     *
     * @param playlist Playlist that should be deleted.
     */
    public void deletePlaylist(Playlist playlist) {
        try {
            playlistManager.deletePlaylist(playlist);
            availablePlaylists.remove(playlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a song to a playlist.
     *
     * @param playlistID The ID of the playlist.
     * @param songID The ID of the song.
     */
    public void addSongToPlaylist(int playlistID, int songID) {
        try {
            playlistManager.addSongToPlaylist(playlistID, songID);
            updatePlaylists();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes song from playlist.
     *
     * @param entryID The song ID depending on which playlist it is in.
     * @param playlistID The playlist ID of the playlist you want a song to be removed from.
     * @throws SQLException If the entryID or playlistID is invalid.
     */
    public void removeSongFromPlaylist(int entryID, int playlistID) throws SQLException {
        try {
            playlistManager.removeSongFromPlaylist(entryID, playlistID);
            updatePlaylists();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the Listview.
     */
    private void updatePlaylists() {
        try {
            List<Playlist> updatedPlaylists = playlistManager.getAllPlaylists();
            availablePlaylists.setAll(updatedPlaylists);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Moves the song up in the Listview of the selected playlist by updating the database.
     *
     * @param playlistID The ID of the selected playlist.
     * @param currentOrder The order number of the song which needs to be moved up.
     * @throws SQLException If the song cannot be moved.
     */
    public void moveSongUp(int playlistID, int currentOrder) throws SQLException{
        playlistManager.moveSongUp(playlistID, currentOrder);
    }

    /**
     * Moves the song down in the Listview of the selected playlist by updating the database.
     *
     * @param playlistID The ID of the selected playlist.
     * @param currentOrder The order number of the song which needs to be moved down.
     * @throws SQLException If the song cannot be moved.
     */
    public void moveSongDown(int playlistID, int currentOrder) throws SQLException{
        playlistManager.moveSongDown(playlistID, currentOrder);
    }

    ////////////////////////
    //// Song Functions ////
    ////////////////////////

    /**
     * Reloads the songs into the Tableview from the database.
     */
    public void refreshSongs() {
        try {
            List<Song> allSongs = songManager.readAllSongs();

            availableSongs.setAll(allSongs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for song in the database.
     *
     * @param query Input from the user.
     * @return A list of all the songs that match the input.
     * @throws Exception If it cannot find the song.
     */
    public List<Song> searchSongs(String query) throws Exception {
        return songManager.searchSongs(query);
    }

    /**
     * Deletes song from the database.
     *
     * @param selectedSong Song that needs to be deleted.
     * @throws Exception If it cannot delete the song.
     */
    public void deleteSong(Song selectedSong)throws Exception {
        // Deletes song in DAL
        songManager.deleteSong(selectedSong);
        //refresh playlists to update their info
        refreshPlaylists();
        // removes it from observable list and UI.
        availableSongs.remove(selectedSong);
    }

    /**
     * Refreshes the playlist tableview.
     */
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


    /**
     * Gets the next song in the playlist.
     *
     * @param playlistId The ID of the selected playlist.
     * @param currentSongId The ID of the current song.
     * @return The next song in the playlist.
     * @throws Exception If the next song cannot be fetched.
     */
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

    /**
     * Get the previous song in the playlist.
     *
     * @param playlistId The ID of the selected playlist.
     * @param currentSongId The ID of the current song.
     * @return The previous song in the playlist.
     * @throws Exception If the previous cannot be fetched.
     */
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
