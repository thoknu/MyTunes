package sample.DAL;

import sample.BE.Playlist;
import sample.BE.SongsInPlaylist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IPlaylistDataAccess {

    private final DatabaseConnector databaseConnector;

    public PlaylistDAO() throws Exception
    {
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
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("PlaylistID");
                    String name = resultSet.getString("Name");
                    int songCount = resultSet.getInt("SongCount");
                    int totalTimeInSeconds = resultSet.getInt("TotalTime");
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
        String sql = "DELETE FROM Playlists WHERE PlaylistID = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
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
                " SELECT PlaylistSongs.EntryID, PlaylistSongs.SongID, Songs.Title, Songs.Time, Songs.Artist " +
                "FROM PlaylistSongs " +
                "INNER JOIN Songs ON PlaylistSongs.SongID = Songs.ID " +
                "WHERE PlaylistID = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, playlist.getId());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int entryID = resultSet.getInt("EntryID");
                    int songID = resultSet.getInt("SongID");
                    String title = resultSet.getString("Title");
                    String artist = resultSet.getString("Artist");

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
        String sql = "INSERT INTO PlaylistSongs (PlaylistID, SongID) VALUES (?, ?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setInt(1, playlistID);
            pstmt.setInt(2, songID);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void removeSongFromPlaylist(int entryID) throws SQLException {
        String sql = "DELETE FROM PlaylistSongs WHERE EntryID = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setInt(1, entryID);
            pstmt.executeUpdate();
        }
    }
}
