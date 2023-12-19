package sample.DAL;


import sample.BE.Song;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SongDAO handles all database operations related to songs.
 */
public class SongDAO implements ISongDataAccess {
    private DatabaseConnector databaseConnector;

    /**
     * Constructor for SongDAO.
     * Initializes the DatabaseConnector for database connections.
     */
    public SongDAO() throws SQLException, IOException {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Reads all songs from the database.
     *
     * @return A list of all songs.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public List<Song> readAllSongs() throws SQLException {
        List<Song> allSongs = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Songs;";
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allSongs.add(extractSongFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Could not retrieve songs from database", e);
        }
        return allSongs;
    }

    /**
     * Retrieves a specific song by its ID.
     *
     * @param songID The ID of the song.
     * @return The song, if found.
     * @throws SQLException for database access errors or if the song is not found.
     */
    public Song getSongByID(int songID) throws SQLException {
        String sql = "SELECT * FROM Songs WHERE ID = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, songID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSongFromResultSet(rs);
                } else {
                    throw new SQLException("Song not found with ID: " + songID);
                }
            }
        }
    }

    /**
     * Creates a new song in the database.
     *
     * @param song The song to be created.
     * @return The newly created song with its generated ID.
     * @throws SQLException for database access errors.
     */
    @Override
    public Song createSong(Song song) throws SQLException {
        String sql = "INSERT INTO dbo.Songs (title, artist, category, time, [file]) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareSongStatement(stmt, song);
            stmt.executeUpdate();
            return new Song(song.getTitle(), song.getArtist(), song.getCategory(), song.getFilePath(), song.getSeconds(), fetchGeneratedId(stmt));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Couldn't create song", e);
        }
    }

    /**
     * Updates an existing song in the database.
     * @param song The song to update.
     * @throws SQLException for database access errors.
     */
    @Override
    public void updateSong(Song song) throws SQLException {
        String sql = "UPDATE dbo.Songs SET Title = ?, Artist = ?, Category = ?, Time = ?, [File] = ? WHERE ID = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            prepareSongStatement(stmt, song);
            stmt.setInt(6, song.getId());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not update song", e);
        }
    }

    /**
     * Deletes a song from the database.
     * @param song The song to be deleted.
     * @throws Exception for database access errors.
     */
    public void deleteSong(Song song) throws Exception {
        String deleteFromPlaylistSQL = "DELETE FROM PlaylistSongs WHERE SongID = ?";
        try (Connection conn = databaseConnector.getConnection()) {
            deleteSongFromPlaylists(conn, song.getId());
            deleteSong(conn, song.getId());
        }catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not delete song", ex);
        }
    }

    ////////////////////////
    //// Helper Methods ////
    ////////////////////////

    /**
     * Extracts song information from ResultSet into a Song object.
     *
     * @param rs ResultSet containing song data.
     * @return A Song object.
     * @throws SQLException for errors in accessing the ResultSet.
     */
    private Song extractSongFromResultSet(ResultSet rs) throws SQLException {
        String title = rs.getString("title");
        String artist = rs.getString("artist");
        String category = rs.getString("category");
        String filePath = rs.getString("file");
        int time = rs.getInt("time");
        int id = rs.getInt("ID");
        return new Song(title, artist, category, filePath, time, id);
    }

    /**
     * Prepares a PreparedStatement with the song data.
     * @param stmt The PreparedStatement to be prepared.
     * @param song The song providing the data.
     * @throws SQLException for statement preparation errors.
     */
    private void prepareSongStatement(PreparedStatement stmt, Song song) throws SQLException {
        stmt.setString(1, song.getTitle());
        stmt.setString(2, song.getArtist());
        stmt.setString(3, song.getCategory());
        stmt.setInt(4, song.getSeconds());
        stmt.setString(5, song.getFilePath());
    }

    /**
     * Fetches the generated ID for a newly inserted row.
     * @param stmt The PreparedStatement used for the insertion.
     * @return The generated ID.
     * @throws SQLException for errors in fetching the generated keys.
     */
    private int fetchGeneratedId(PreparedStatement stmt) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to fetch generated ID.");
            }
        }
    }

    /**
     * Deletes a song from all playlists.
     * @param conn The database connection.
     * @param songId The ID of the song to delete.
     * @throws SQLException for database access errors.
     */
    private void deleteSongFromPlaylists(Connection conn, int songId) throws SQLException {
        String sql = "DELETE FROM PlaylistSongs WHERE SongID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, songId);
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a song from the database.
     * @param conn The database connection.
     * @param songId The ID of the song to delete.
     * @throws SQLException for database access errors.
     */
    private void deleteSong(Connection conn, int songId) throws SQLException {
        String sql = "DELETE FROM Songs WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, songId);
            stmt.executeUpdate();
        }
    }

    /**
     * Reads all song files from the local 'songs' directory.
     *
     * @return ArrayList<File> A list containing all song files in the directory.
     */
    public ArrayList<File> readLocalSongs() {
        ArrayList<File> songs = new ArrayList<File>();
        File directory = new File("songs");

        // Retrieve all files within the 'songs' directory
        File[] files = directory.listFiles();
        if (files != null) {
            // Add each file to the songs list
            for (File file : files) {
                songs.add(file);
            }
        }
        return songs;
    }

    /**
     * Reads a specific song file based on its index in the local 'songs' directory.
     *
     * @param index The index of the song file in the directory.
     * @return File The song file at the specified index, or the directory itself if index is invalid.
     */
    public File readLocalSong(int index) {
        File directory = new File("songs");

        // Retrieve all files within the 'songs' directory
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (index == i) {
                    // Return the file at the specified index
                    return files[i];
                }
            }
        }
        // Return the directory itself if the index is out of bounds
        return directory;
    }


}
