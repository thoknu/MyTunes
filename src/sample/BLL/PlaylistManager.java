package sample.BLL;

import sample.BE.Playlist;
import sample.BE.Song;
import sample.DAL.PlaylistDAO;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private PlaylistDAO playlistDAO;

    public PlaylistManager() {
        playlistDAO = new PlaylistDAO();
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        return playlistDAO.readAllPlaylists();
    }

    public List<Song> getAllSongsInPlaylist(Playlist playlist) throws Exception {
        List<Song> readSongs = playlistDAO.readAllSongsInPlaylist(playlist);
        List<Song> songs = new ArrayList<>();
        for (Song s : readSongs) {
            songs.add(s);
        }
        return null;
    }

    public Song getSong(Playlist playlist, Song song) throws Exception {
        for (Song s : playlistDAO.readAllSongsInPlaylist(playlist)) {
            if (s.getId() == song.getId()) {
                return s;
            }
        }
        return null;
    }

    public void CalculateDurationOfSong(Song song) {

    }

    public void CalculateDurationOfPlaylist(Playlist playlist) {

    }
}
