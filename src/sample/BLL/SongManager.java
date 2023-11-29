package sample.BLL;

import sample.BE.Song;
import sample.DAL.ISongDataAccess;
import sample.DAL.SongDAO;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private ISongDataAccess songDAO;
    private Searcher songSearcher = new Searcher();

    public SongManager() throws Exception {
        songDAO = new SongDAO();
    }

    public List<Song> readAllSongs() throws Exception {
        return songDAO.readAllSongs();
    }

    public Song createNewSong(Song newSong) throws Exception{
        return songDAO.createSong(newSong);
    }

    public void updateSong (Song selectedSong) throws Exception{
        songDAO.updateSong(selectedSong);
    }
    public void deleteSong(Song selectedSong) throws Exception{
        songDAO.deleteSong(selectedSong);
    }
    public List<Song> searchSongs (String query) throws Exception {
        List<Song> allSongs = readAllSongs();
        List<Song> searchResult = songSearcher.search(allSongs,query);
        return searchResult;
    }

}
