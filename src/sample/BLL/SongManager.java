package sample.BLL;

import sample.BE.Song;
import sample.DAL.SongDAO;

import java.util.List;

public class SongManager {

    private SongDAO songDAO;

    public SongManager() {
        songDAO = new SongDAO();
    }

    public List<Song> getAllSongs() throws Exception {
        return songDAO.readAllSongs();
    }
}
