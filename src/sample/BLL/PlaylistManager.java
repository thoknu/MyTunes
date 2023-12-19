package sample.BLL;

import sample.BE.Playlist;
import sample.BE.SongsInPlaylist;
import sample.DAL.PlaylistDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Manages all playlist related operations.
 */
public class PlaylistManager {
    private final PlaylistDAO playlistDAO;

    /**
     * Constructs the PlaylistManager and initializes the PlaylistDAO.
     * @throws Exception if an error occurs during DAO initialization.
     */
    public PlaylistManager() throws Exception {
        this.playlistDAO = new PlaylistDAO();
    }

    /**
     * Creates a new playlist with the given name.
     * @param name the name of the new playlist.
     * @return the created Playlist object.
     * @throws SQLException if a database access error occurs.
     */
    public Playlist createPlaylist(String name) throws SQLException {
        return playlistDAO.createPlaylist(name);
    }

    /**
     * Retrieves all playlists.
     * @return a list of all playlists.
     * @throws Exception if an error occurs during data retrieval.
     */
    public List<Playlist> getAllPlaylists() throws Exception {
        return playlistDAO.readAllPlaylists();
    }

    /**
     * Updates a selected playlist.
     * @param selectedPlaylist the playlist to be updated.
     * @throws IllegalArgumentException if selectedPlaylist is null.
     * @throws Exception if an error occurs during the update.
     */
    public void editPlaylist(Playlist selectedPlaylist) throws Exception {
        if (selectedPlaylist == null) {
            throw new IllegalArgumentException("Selected playlist cannot be null.");
        }
        playlistDAO.updatePlaylist(selectedPlaylist);
    }

    /**
     * Deletes a specified playlist.
     * @param playlist the playlist to be deleted.
     * @throws IllegalArgumentException if playlist is null.
     * @throws SQLException if a database access error occurs.
     */
    public void deletePlaylist(Playlist playlist) throws SQLException {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist to delete cannot be null.");
        }
        playlistDAO.deletePlaylist(playlist.getId());
    }

    /**
     * Retrieves all songs in a specific playlist.
     * @param playlist the playlist whose songs are to be retrieved.
     * @return a list of songs in the specified playlist.
     * @throws IllegalArgumentException if playlist is null.
     * @throws Exception if an error occurs during data retrieval.
     */
    public List<SongsInPlaylist> getAllSongsInPlaylist(Playlist playlist) throws Exception {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null.");
        }
        return playlistDAO.readAllSongsInPlaylist(playlist);
    }

    /**
     * Adds a song to a playlist.
     * @param playlistID the ID of the playlist.
     * @param songID the ID of the song to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addSongToPlaylist(int playlistID, int songID) throws SQLException {
        playlistDAO.addSongToPlaylist(playlistID, songID);
    }

    /**
     * Removes a song from a playlist.
     * @param entryID the entry ID of the song in the playlist.
     * @param playlistID the ID of the playlist.
     * @throws SQLException if a database access error occurs.
     */
    public void removeSongFromPlaylist(int entryID, int playlistID) throws SQLException {
        playlistDAO.removeSongFromPlaylist(entryID, playlistID);
    }

}
