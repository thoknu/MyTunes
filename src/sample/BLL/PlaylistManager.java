package sample.BLL;

import sample.BE.Playlist;
import sample.BE.SongsInPlaylist;
import sample.DAL.PlaylistDAO;

import java.sql.SQLException;
import java.util.List;

public class PlaylistManager {
    private final PlaylistDAO playlistDAO;

    public PlaylistManager() throws Exception {
        this.playlistDAO = new PlaylistDAO();
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        return playlistDAO.readAllPlaylists();
    }

    public Playlist createPlaylist(String name) throws SQLException {
        return playlistDAO.createPlaylist(name);
    }

    public void editPlaylist(Playlist selectedPlaylist) throws Exception {
        if (selectedPlaylist == null) {
            throw new IllegalArgumentException("Selected playlist cannot be null.");
        }
        playlistDAO.updatePlaylist(selectedPlaylist);
    }

    public void deletePlaylist(Playlist playlist) throws SQLException {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist to delete cannot be null.");
        }
        playlistDAO.deletePlaylist(playlist.getId());
    }

    public List<SongsInPlaylist> getAllSongsInPlaylist(Playlist playlist) throws Exception {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null.");
        }
        return playlistDAO.readAllSongsInPlaylist(playlist);
    }

    public void addSongToPlaylist(int playlistID, int songID) throws SQLException {
        playlistDAO.addSongToPlaylist(playlistID, songID);
    }

    public void removeSongFromPlaylist(int entryID, int playlistID) throws SQLException {
        playlistDAO.removeSongFromPlaylist(entryID, playlistID);
    }

    public void moveSongUp(int playlistID, int currentOrder) throws SQLException{
        if (currentOrder > 0){
            playlistDAO.swapSongOrder(playlistID, currentOrder, currentOrder - 1);
        }
    }

    public void moveSongDown(int playlistID, int currentOrder) throws SQLException{
        int maxOrder = playlistDAO.getMaxOrderInPlaylist(playlistID);
        if (currentOrder < maxOrder){
            playlistDAO.swapSongOrder(playlistID, currentOrder, currentOrder + 1);
        }
    }

}
