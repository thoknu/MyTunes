package sample.DAL;

import sample.BE.Playlist;
import sample.BE.SongsInPlaylist;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PlaylistDAO is responsible for handling all database operations related to playlists.
 */
public class PlaylistDAO implements IPlaylistDataAccess {
    private final DatabaseConnector databaseConnector;

    /**
     * Constructor for PlaylistDAO.
     * Initializes the DatabaseConnector to establish database connections.
     */
    public PlaylistDAO() throws Exception {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * Creates a new playlist in the database with the specified name.
     *
     * @param name The name of the new playlist.
     * @return Playlist The newly created playlist.
     * @throws SQLException if there is an issue executing the query.
     */
    @Override
    public Playlist createPlaylist(String name) throws SQLException {
        String sql = "INSERT INTO Playlists (Name) VALUES (?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            executeUpdateAndValidate(stmt);
            return new Playlist(fetchGeneratedId(stmt), name, 0, 0);
        }
    }

    /**
     * Reads all playlists from the database.
     *
     * @return List<Playlist> A list of all playlists.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public List<Playlist> readAllPlaylists() throws SQLException {
        List<Playlist> allPlaylists = new ArrayList<>();
        String sql = "SELECT p.PlaylistID, p.Name, COUNT(ps.SongID) AS SongCount, SUM(s.Time) AS TotalTime " +
                "FROM Playlists p " +
                "LEFT JOIN PlaylistSongs ps ON p.PlaylistID = ps.PlaylistID " +
                "LEFT JOIN Songs s ON ps.SongID = s.ID " +
                "GROUP BY p.PlaylistID, p.Name";
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allPlaylists.add(extractPlaylistFromResultSet(rs));
            }
        }
        return allPlaylists;
    }

    /**
     * Updates the specified playlist in the database.
     *
     * @param selectedPlaylist The playlist to be updated.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public void updatePlaylist(Playlist selectedPlaylist) throws SQLException {
        String sql = "UPDATE Playlists SET Name = ? WHERE PlaylistID = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, selectedPlaylist.getName());
            stmt.setInt(2, selectedPlaylist.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes the specified playlist from the database.
     *
     * @param id The ID of the playlist to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public void deletePlaylist(int id) throws SQLException {
        try (Connection conn = databaseConnector.getConnection()) {
            deletePlaylistSongs(conn, id);
            deletePlaylist(conn, id);
        }
    }

    /**
     * Reads all songs in a given playlist.
     *
     * @param playlist The playlist from which songs are to be read.
     * @return List<SongsInPlaylist> A list of songs in the specified playlist.
     * @throws SQLException If a database access error occurs.
     */

    public List<SongsInPlaylist> readAllSongsInPlaylist(Playlist playlist) throws SQLException {
        List<SongsInPlaylist> allSongsInPlaylist = new ArrayList<>();
        String sql = "SELECT PlaylistSongs.EntryID, PlaylistSongs.SongID, Songs.Title, Songs.Artist " +
                "FROM PlaylistSongs " +
                "INNER JOIN Songs ON PlaylistSongs.SongID = Songs.ID " +
                "WHERE PlaylistID = ? " +
                "ORDER BY PlaylistSongs.[Order];";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlist.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    allSongsInPlaylist.add(extractSongInPlaylistFromResultSet(rs, playlist.getId()));
                }
            }
        }
        return allSongsInPlaylist;
    }

    /**
     * Adds a song to a playlist in the database.
     *
     * @param playlistID The ID of the playlist.
     * @param songID The ID of the song to be added.
     * @throws SQLException If a database access error occurs.
     */

    public void addSongToPlaylist(int playlistID, int songID) throws SQLException {
        String sql = "INSERT INTO PlaylistSongs (PlaylistID, SongID, [Order]) " +
                "VALUES (?, ?, (SELECT COUNT(*) FROM PlaylistSongs WHERE PlaylistID = ?));";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistID);
            stmt.setInt(2, songID);
            stmt.setInt(3, playlistID);
            stmt.executeUpdate();
        }
    }

    /**
     * Removes a song from a playlist in the database.
     *
     * @param entryID The entry ID of the song in the playlist.
     * @param playlistID The ID of the playlist.
     * @throws SQLException If a database access error occurs.
     */

    public void removeSongFromPlaylist(int entryID, int playlistID) throws SQLException {
        try (Connection conn = databaseConnector.getConnection()) {
            int deletedSongOrder = getSongOrderBeforeDelete(conn, entryID);
            deleteSongFromPlaylist(conn, entryID);
            updateSongOrderAfterDelete(conn, playlistID, deletedSongOrder);
        }
    }

    /**
     * Moves a song up in the order within a playlist.
     *
     * @param playlistID The ID of the playlist containing the song.
     * @param currentOrder The current order of the song in the playlist.
     * @throws SQLException If an error occurs during the order update operation.
     */
    public void moveSongUp(int playlistID, int currentOrder) throws SQLException{
        if (currentOrder > 0){
            swapSongOrder(playlistID, currentOrder, currentOrder - 1);
        }
    }

    /**
     * Moves a song down in the order within a playlist.
     *
     * @param playlistID The ID of the playlist containing the song.
     * @param currentOrder The current order of the song in the playlist.
     * @throws SQLException If an error occurs during the order update operation.
     */
    public void moveSongDown(int playlistID, int currentOrder) throws SQLException{
        int maxOrder = getMaxOrderInPlaylist(playlistID);
        if (currentOrder < maxOrder){
            swapSongOrder(playlistID, currentOrder, currentOrder + 1);
        }
    }


    ////////////////////////
    //// Helper Methods ////
    ////////////////////////

    /**
     * Executes an update using the given PreparedStatement and validates the outcome.
     *
     * @param stmt The PreparedStatement to be executed.
     * @throws SQLException If no rows are affected by the update, indicating a failure in the operation.
     */
    private void executeUpdateAndValidate(PreparedStatement stmt) throws SQLException {
        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating playlist failed, no rows affected.");
        }
    }

    /**
     * Retrieves the auto-generated key from a PreparedStatement.
     *
     * @param stmt The PreparedStatement used for the insert operation.
     * @return The generated key for the newly inserted record.
     * @throws SQLException If no generated key is found, indicating the insert operation didn't succeed.
     */
    private int fetchGeneratedId(PreparedStatement stmt) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating playlist failed, no ID obtained.");
            }
        }
    }

    /**
     * Extracts a Playlist object from the current row of a ResultSet.
     *
     * @param rs The ResultSet from which playlist data is to be extracted.
     * @return The constructed Playlist object.
     * @throws SQLException If there's an error reading from the ResultSet.
     */
    private Playlist extractPlaylistFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("PlaylistID");
        String name = rs.getString("Name");
        int songCount = rs.getInt("SongCount");
        int totalTimeInSeconds = rs.getInt("TotalTime");
        return new Playlist(id, name, songCount, totalTimeInSeconds);
    }

    /**
     * Deletes all song entries from a playlist in the database.
     * Used to clear associations in PlaylistSongs before deleting a playlist.
     *
     * @param conn The database connection to use for the operation.
     * @param playlistId The ID of the playlist from which songs are to be deleted.
     * @throws SQLException If an error occurs during the delete operation.
     */
    private void deletePlaylistSongs(Connection conn, int playlistId) throws SQLException {
        String deleteSongSql = "DELETE FROM PlaylistSongs WHERE PlaylistID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSongSql)) {
            stmt.setInt(1, playlistId);
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a specific playlist from the database.
     *
     * @param conn The database connection to use for the operation.
     * @param playlistId The ID of the playlist to be deleted.
     * @throws SQLException If an error occurs during the delete operation.
     */
    private void deletePlaylist(Connection conn, int playlistId) throws SQLException {
        String deletePlaylistSql = "DELETE FROM Playlists WHERE PlaylistID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deletePlaylistSql)) {
            stmt.setInt(1, playlistId);
            stmt.executeUpdate();
        }
    }

    /**
     * Extracts a SongsInPlaylist object from the current row of a ResultSet.
     *
     * @param rs The ResultSet from which song data is to be extracted.
     * @param playlistId The ID of the playlist to which the song belongs.
     * @return SongsInPlaylist The constructed SongsInPlaylist object.
     * @throws SQLException If there's an error reading the ResultSet.
     */
    private SongsInPlaylist extractSongInPlaylistFromResultSet(ResultSet rs, int playlistId) throws SQLException {
        int entryID = rs.getInt("EntryID");
        int songID = rs.getInt("SongID");
        String title = rs.getString("Title");
        String artist = rs.getString("Artist");
        return new SongsInPlaylist(entryID, playlistId, songID, title, artist);
    }

    /**
     * Retrieves the order of a song in a playlist before deletion.
     *
     * @param connection The database connection to use for the query.
     * @param entryID The entry ID of the song in the playlist.
     * @return int The order of the song before deletion.
     * @throws SQLException If the song is not found or there's an error in the query.
     */
    private int getSongOrderBeforeDelete(Connection connection, int entryID) throws SQLException {
        String sql = "SELECT [Order] FROM PlaylistSongs WHERE EntryID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, entryID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Order");
            } else {
                throw new SQLException("Song not found with EntryID: " + entryID);
            }
        }
    }

    /**
     * Deletes a specific song entry from a playlist in the database.
     *
     * @param connection The database connection to use for the delete operation.
     * @param entryID The entry ID of the song in the playlist to be deleted.
     * @throws SQLException If an error occurs during the delete operation.
     */
    private void deleteSongFromPlaylist(Connection connection, int entryID) throws SQLException {
        String sql = "DELETE FROM PlaylistSongs WHERE EntryID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, entryID);
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates the order of songs in a playlist after a song deletion.
     *
     * @param connection The database connection to use for the update.
     * @param playlistID The ID of the playlist in which order is to be updated.
     * @param deletedSongOrder The original order of the deleted song.
     * @throws SQLException If an error occurs during the update operation.
     */
    private void updateSongOrderAfterDelete(Connection connection, int playlistID, int deletedSongOrder) throws SQLException {
        String sql = "UPDATE PlaylistSongs SET [Order] = [Order] - 1 WHERE PlaylistID = ? AND [Order] > ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playlistID);
            pstmt.setInt(2, deletedSongOrder);
            pstmt.executeUpdate();
        }
    }

    /**
     * Swaps the order of two songs in a playlist.
     *
     * @param playlistID The ID of the playlist where the songs are to be swapped.
     * @param order1 The order value of the first song to be swapped.
     * @param order2 The order value of the second song to be swapped.
     * @throws SQLException If an error occurs during the update operation.
     */

    public void swapSongOrder(int playlistID, int order1, int order2) throws SQLException {
        String sql1 = "UPDATE PlaylistSongs SET [Order] = -1 WHERE PlaylistID = ? AND [Order] = ?";
        String sql2 = "UPDATE PlaylistSongs SET [Order] = ? WHERE PlaylistID = ? AND [Order] = ?";
        String sql3 = "UPDATE PlaylistSongs SET [Order] = ? WHERE PlaylistID = ? AND [Order] = -1";

        try (Connection connection = databaseConnector.getConnection()) {

            try (PreparedStatement pstmt1 = connection.prepareStatement(sql1);
                 PreparedStatement pstmt2 = connection.prepareStatement(sql2);
                 PreparedStatement pstmt3 = connection.prepareStatement(sql3)) {

                // Set the order of the first song to a temporary value
                pstmt1.setInt(1, playlistID);
                pstmt1.setInt(2, order1);
                pstmt1.executeUpdate();

                // Swap orders
                pstmt2.setInt(1, order1);
                pstmt2.setInt(2, playlistID);
                pstmt2.setInt(3, order2);
                pstmt2.executeUpdate();

                pstmt3.setInt(1, order2);
                pstmt3.setInt(2, playlistID);
                pstmt3.executeUpdate();

            } catch (SQLException e) {
                throw new SQLException("Error swapping song order in playlist", e);
            }
        }
    }

    /**
     * Retrieves the maximum order value of songs in a specific playlist.
     *
     * @param playlistID The ID of the playlist.
     * @return The maximum order value of songs in the playlist.
     * @throws SQLException If an error occurs during the query.
     */
    public int getMaxOrderInPlaylist(int playlistID) throws SQLException{
        String sql = "SELECT MAX([Order]) FROM PlaylistSongs WHERE PlaylistID = ?;";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setInt(1, playlistID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        }
    }


}
