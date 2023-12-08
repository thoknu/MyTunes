package sample.DAL;

import sample.BE.Song;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ISongDataAccess {
    public List<Song> readAllSongs() throws Exception;

    public Song readSong(Song song) throws Exception;

    public ArrayList<File> readLocalSongs() throws IOException;

    public Song createSong(Song song) throws Exception;

    public void updateSong(Song song) throws Exception;

    public void deleteSong(Song song) throws Exception;
}
