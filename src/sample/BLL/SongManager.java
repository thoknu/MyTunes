package sample.BLL;

import sample.BE.Song;
import sample.DAL.ISongDataAccess;
import sample.DAL.SongDAO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages song-related operations, interfacing with the data access layer.
 */
public class SongManager {

    private final SongDAO songDAO;
    private Searcher songSearcher = new Searcher();

    public SongManager() throws SQLException, IOException {
        songDAO = new SongDAO();
    }

    /**
     * Retrieves all songs.
     *
     * @return a list of all songs.
     * @throws SQLException if a database access error occurs.
     */
    public List<Song> readAllSongs() throws Exception {
        return songDAO.readAllSongs();
    }

    /**
     * Creates a new song.
     *
     * @param newSong the song to create.
     * @return the created song.
     * @throws SQLException if a database access error occurs.
     */
    public Song createNew0Song(Song newSong) throws SQLException {
        return songDAO.createSong(newSong);
    }

    /**
     * Updates an existing song.
     *
     * @param selectedSong the song to update.
     * @throws SQLException if a database access error occurs.
     */
    public void updateSong(Song selectedSong) throws SQLException {
        songDAO.updateSong(selectedSong);
    }

    /**
     * Deletes a song.
     *
     * @param selectedSong the song to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteSong(Song selectedSong) throws SQLException {
        songDAO.deleteSong(selectedSong);
    }

    /**
     * Searches for songs based on a query.
     *
     * @param query the search query.
     * @return a list of songs that match the query.
     * @throws SQLException if a database access error occurs.
     */
    public List<Song> searchSongs(String query) throws Exception {
        List<Song> allSongs = readAllSongs();
        return songSearcher.search(allSongs, query);
    }

    /**
     * Retrieves a song by its ID.
     *
     * @param songID the ID of the song.
     * @return the song.
     * @throws SQLException if a database access error occurs.
     */
    public Song getSongByID(int songID) throws SQLException {
        return songDAO.getSongByID(songID);
    }
}
