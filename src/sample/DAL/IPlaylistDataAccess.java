package sample.DAL;

import sample.BE.Playlist;
import sample.BE.SongsInPlaylist;

import java.sql.SQLException;
import java.util.List;

public interface IPlaylistDataAccess {
    public List<Playlist> readAllPlaylists();

    public Playlist createPlaylist(String name) throws SQLException;

    public void updatePlaylist(Playlist selectedPlaylist) throws Exception;

    public void deletePlaylist(int id) throws Exception;

    //public Playlist readPlaylist(Playlist playlist) throws Exception;

    public List<SongsInPlaylist> readAllSongsInPlaylist(Playlist playlist) throws Exception;

    //public Song readSongInPlaylist(Playlist playlist, Song song) throws Exception;

    public void addSongToPlaylist(int playlistID, int songID) throws SQLException;

    public void removeSongFromPlaylist(int entryID) throws SQLException;

}
