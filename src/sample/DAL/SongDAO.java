package sample.DAL;


import sample.BE.Song;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO implements ISongDataAccess {

    private DatabaseConnector databaseConnector;
    public SongDAO() throws Exception {
        databaseConnector = new DatabaseConnector();
    }
    @Override
    public List<Song> readAllSongs() throws Exception {
        ArrayList<Song> allSongs = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM dbo.Songs;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String category = rs.getString("category");
                int time = rs.getInt("time"); // in seconds
                int id = rs.getInt("ID");
                String filepath = rs.getString("filename");

                Song song = new Song(title,artist,category,filepath, time, id);

                allSongs.add(song);
            }
            return allSongs;
        }
        catch (SQLException exception){
            exception.printStackTrace();
            throw new Exception("Could not get songs from database");
        }
    }

    @Override
    public Song readSong(Song song) throws Exception {
        String sql = "SELECT * FROM dbo.Songs WHERE ID = song.getId();";

        try (Connection conn = databaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                song.setTitle(rs.getString("title"));
                song.setArtist(rs.getString("artist"));
                song.setCategory(rs.getString("category"));
                song.setSeconds(rs.getInt("time"));
                song.setId(rs.getInt("ID"));
            }
            return song;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not get song from database", e);
        }
    }

    public ArrayList<File> readLocalSongs() {
        ArrayList<File> songs = new ArrayList<File>();
        File directory = new File("songs");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                songs.add(file);
            }
        }
        return songs;
    }

    public File readLocalSong(int index) {
        File directory = new File("songs");
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (index == i) {
                    return files[i];
                }
            }
        }
        return directory;
    }

    @Override
    public Song createSong(Song song) throws Exception {

        String sql = "INSERT INTO dbo.Songs (title,artist,category,time) VALUES (?,?,?,?);";

        try (Connection conn = databaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1,song.getTitle());
            stmt.setString(2,song.getArtist());
            stmt.setString(3,song.getCategory());
            stmt.setInt(4,song.getSeconds());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if(rs.next()){ id = rs.getInt(1);}

            Song createdSong = new Song(song.getTitle(), song.getArtist(), song.getCategory(),song.getFilePath(), song.getSeconds(), id);

            return createdSong;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Couldn't create song", ex);
        }

    }

    @Override
    public void updateSong(Song song) throws Exception {
    // SQL command
        String sql = "UPDATE dbo.Songs SET Title = ?, Artist = ?, Category = ?, Time = ?, Filename = ? WHERE ID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            // Bind the parameters
            stmt.setString(1,song.getTitle());
            stmt.setString(2,song.getArtist());
            stmt.setString(3,song.getCategory());
            stmt.setInt(4,song.getSeconds());
            stmt.setString(5,song.getFilePath());
            stmt.setInt(6,song.getId());


            stmt.executeUpdate();

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update song", ex);
        }
    }

    @Override
    public void deleteSong(Song song) throws Exception {

        String sql = "DELETE FROM dbo.Songs WHERE ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1,song.getId());

            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not delete song", ex);
        }
    }
}
