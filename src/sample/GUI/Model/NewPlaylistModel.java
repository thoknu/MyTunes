package sample.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.BE.Playlist;
import sample.BLL.PlaylistManager;

import java.sql.SQLException;
import java.util.List;

public class NewPlaylistModel {

    private PlaylistManager playlistManager;
    private ObservableList<Playlist> availablePlaylists;

    /**
     * Creates the PlaylistManager object and gets playlists in the Tableview.
     *
     * @throws Exception If the object cannot be created or the playlists cannot be fetched.
     */
    public NewPlaylistModel() throws Exception {
        playlistManager = new PlaylistManager();
        availablePlaylists = FXCollections.observableArrayList(playlistManager.getAllPlaylists());
    }

    /**
     * Creates a playlist with the input name.
     *
     * @param name The user input name of the playlist.
     * @return The playlist so it can be displayed.
     * @throws SQLException If the name is invalid.
     */
    public Playlist createPlaylist(String name) throws SQLException {
        try  {
            return playlistManager.createPlaylist(name);
        } finally {
            refreshPlaylists();
        }
    }

    /**
     * Edits the selected playlist.
     *
     * @param selectedPlaylist The selected playlist which needs to be edited.
     */
    public void editPlaylist(Playlist selectedPlaylist){
        try {
            playlistManager.editPlaylist(selectedPlaylist);
            availablePlaylists.set(availablePlaylists.indexOf(selectedPlaylist) + 1, selectedPlaylist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows fetching of the current ObservableList of playlists.
     *
     * @return A list of Playlists for displaying.
     */
    public ObservableList<Playlist> getObservablePlaylists() {
        return availablePlaylists;
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

}
