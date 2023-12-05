package sample.DAL;

import sample.BE.Song;

import java.util.List;

public interface ISongDataAccess {
    public List<Song> readAllSongs() throws Exception;

    public Song readSong(Song song) throws Exception;

    public Song createSong(Song song) throws Exception;

    public void updateSong(Song song) throws Exception;

    public void deleteSong(Song song) throws Exception;
}
