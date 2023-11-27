package sample.DAL;

import sample.BE.Playlist;
import sample.BE.Song;

import java.util.List;

public interface IPlaylistDataAccess {
    public List<Playlist> readAllPlaylists() throws Exception;

    public Playlist readPlaylist(Playlist playlist) throws Exception;

    public List<Song> readAllSongsInPlaylist(Playlist playlist) throws Exception;

    public Song readSongInPlaylist(Playlist playlist, Song song) throws Exception;

    public void createPlaylist(Playlist playlist) throws Exception;

    public void updatePlaylist(Playlist playlist) throws Exception;

    public void deletePlaylist(Playlist playlist) throws Exception;
}
