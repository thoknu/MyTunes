package sample.BLL;

import sample.BE.Song;
import sample.DAL.SongDAO;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private SongDAO songDAO;

    public SongManager() throws Exception {
        songDAO = new SongDAO();
    }

    public List<Song> getAllSongs() throws Exception {
        return songDAO.readAllSongs();
    }



}
