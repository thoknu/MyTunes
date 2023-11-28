package sample.DAL;

import sample.BE.Duration;
import sample.BE.Song;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                int id = rs.getInt("ID");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String category = rs.getString("category");
                int time = rs.getInt("time"); // in seconds

                Song song = new Song(title,artist,category,"", time, id);
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
        return null;
    }

    @Override
    public void createSong(Song song) throws Exception {



        // Calculate duration in seconds
       // Duration songDuration = new Duration(song.getMinute(), song.getSecond());
      //  int durationInSeconds = songDuration.getDurationInSecondsSong();
    }

    @Override
    public void updateSong(Song song) throws Exception {

    }

    @Override
    public void deleteSong(Song song) throws Exception {

    }
}
