package sample.DAL;

import sample.BE.Song;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ISongDataAccess {

    public Song createSong(Song song) throws Exception;

    public List<Song> readAllSongs() throws SQLException;

    public void updateSong(Song song) throws Exception;

    public void deleteSong(Song song) throws Exception;

    public Song getSongByID(int songID) throws SQLException;
}
