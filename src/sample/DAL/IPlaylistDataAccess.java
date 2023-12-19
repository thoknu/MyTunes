package sample.DAL;

import sample.BE.Playlist;
import sample.BE.SongsInPlaylist;

import java.sql.SQLException;
import java.util.List;

public interface IPlaylistDataAccess {

    public Playlist createPlaylist(String name) throws SQLException;

    public List<Playlist> readAllPlaylists() throws SQLException;

    public void updatePlaylist(Playlist selectedPlaylist) throws Exception;

    public void deletePlaylist(int id) throws Exception;

    public List<SongsInPlaylist> readAllSongsInPlaylist(Playlist playlist) throws Exception;

    public void addSongToPlaylist(int playlistID, int songID) throws SQLException;

    public void removeSongFromPlaylist(int entryID, int playlistID) throws SQLException;

    public void swapSongOrder(int playlistID, int order1, int order2) throws SQLException;
}
