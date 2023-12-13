package sample.DAL;

import sample.BE.Playlist;
import sample.BE.SongsInPlaylist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IPlaylistDataAccess {

    private final DatabaseConnector databaseConnector;

    public PlaylistDAO() throws Exception {
        databaseConnector = new DatabaseConnector();
    }


    @Override
    public Playlist createPlaylist(String name) throws SQLException {
        int id = 0; // This will be replaced with the generated ID from the database
        int songCount = 0; // A new playlist has no songs
        int totalTimeInSeconds = 0; // Total time is also 0 for a new playlist

        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Playlists (Name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating playlist failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating playlist failed, no ID obtained.");
                }
            }
        }

        return new Playlist(id, name, songCount, totalTimeInSeconds);
    }

    @Override
    public List<Playlist> readAllPlaylists() {
        List<Playlist> allPlaylists = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sql =
                    "SELECT p.PlaylistID, p.Name, COUNT(ps.SongID) AS SongCount, SUM(s.Time) AS TotalTime " +
                            "FROM Playlists p " +
                            "LEFT JOIN PlaylistSongs ps ON p.PlaylistID = ps.PlaylistID " +
                            "LEFT JOIN Songs s ON ps.SongID = s.ID " +
                            "GROUP BY p.PlaylistID, p.Name";
            //
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("PlaylistID");
                    String name = rs.getString("Name");
                    int songCount = rs.getInt("SongCount");
                    int totalTimeInSeconds = rs.getInt("TotalTime");
                    Playlist playlist = new Playlist(id, name, songCount, totalTimeInSeconds);
                    allPlaylists.add(playlist);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetchign playlists:" + e.getMessage(), e);
        }
        return allPlaylists;
    }

    @Override
    public void updatePlaylist(Playlist selectedPlaylist) throws Exception {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Playlists SET Name = ? WHERE PlaylistID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, selectedPlaylist.getName());
                pstmt.setInt(2, selectedPlaylist.getId());
                pstmt.executeUpdate();
            }
        }
    }

    @Override
    public void deletePlaylist(int id) throws SQLException {
        String deleteSongssql ="DELETE FROM PlaylistSongs WHERE PlaylistID = ?";
        String deletePlaylistsql = "DELETE FROM Playlists WHERE PlaylistID = ?";

        try (Connection connection = databaseConnector.getConnection()) {
            //Delete all songs in the playlist
            try (PreparedStatement pstmtDeleteSongs = connection.prepareStatement(deleteSongssql)){
                pstmtDeleteSongs.setInt(1, id);
                pstmtDeleteSongs.executeUpdate();
            }
            //Delete the playlist
            try (PreparedStatement pstmtDeletePlaylist = connection.prepareStatement(deletePlaylistsql)){
                pstmtDeletePlaylist.setInt(1, id);
                pstmtDeletePlaylist.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SongsInPlaylist> readAllSongsInPlaylist(Playlist playlist) throws SQLException {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null.");
        }

        List<SongsInPlaylist> allSongsInPlaylist = new ArrayList<>();
        String sql =
                "SELECT PlaylistSongs.EntryID, PlaylistSongs.SongID, Songs.Title, Songs.Time, Songs.Artist " +
                        "FROM PlaylistSongs " +
                        "INNER JOIN Songs ON PlaylistSongs.SongID = Songs.ID " +
                        "WHERE PlaylistID = ? " +
                        "ORDER BY PlaylistSongs.[Order];";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, playlist.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int entryID = rs.getInt("EntryID");
                    int songID = rs.getInt("SongID");
                    String title = rs.getString("Title");
                    String artist = rs.getString("Artist");

                    SongsInPlaylist songsInPlaylist = new SongsInPlaylist(entryID, playlist.getId(), songID, title, artist);
                    allSongsInPlaylist.add(songsInPlaylist);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allSongsInPlaylist;
    }

    @Override
    public void addSongToPlaylist(int playlistID, int songID) throws SQLException {
        String sql = "INSERT INTO PlaylistSongs (PlaylistID, SongID, [Order]) " +
                "VALUES (?, ?, (SELECT COUNT(*) FROM PlaylistSongs WHERE PlaylistID = ?));";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playlistID);
            pstmt.setInt(2, songID);
            pstmt.setInt(3, playlistID);

            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeSongFromPlaylist(int entryID, int playlistID) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {

            int deletedSongOrder = getSongOrderBeforeDelete(connection, entryID);
            deleteSongFromPlaylist(connection, entryID);
            updateSongOrderAfterDelete(connection, playlistID, deletedSongOrder);

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting song from playlist", e);
        }
    }

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

    private void deleteSongFromPlaylist(Connection connection, int entryID) throws SQLException {
        String sql = "DELETE FROM PlaylistSongs WHERE EntryID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, entryID);
            pstmt.executeUpdate();
        }
    }

    private void updateSongOrderAfterDelete(Connection connection, int playlistID, int deletedSongOrder) throws SQLException {
        String sql = "UPDATE PlaylistSongs SET [Order] = [Order] - 1 WHERE PlaylistID = ? AND [Order] > ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playlistID);
            pstmt.setInt(2, deletedSongOrder);
            pstmt.executeUpdate();
        }
    }

    @Override
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
                e.printStackTrace();
            }
        }
    }

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
