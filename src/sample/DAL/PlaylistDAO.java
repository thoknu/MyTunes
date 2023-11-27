package sample.DAL;

import sample.BE.Playlist;
import sample.BE.Song;

import java.util.List;

public class PlaylistDAO implements IPlaylistDataAccess {

    @Override
    public List<Playlist> readAllPlaylists() throws Exception {
        return null;
    }

    @Override
    public List<Song> readAllSongsInPlaylist(Playlist playlist) throws Exception {
        return null;
    }

    @Override
    public Song readSongInPlaylist(Playlist playlist, Song song) throws Exception {
        return null;
    }

    @Override
    public Playlist readPlaylist(Playlist playlist) throws Exception {
        return null;
    }

    @Override
    public void createPlaylist(Playlist playlist) throws Exception {

    }

    @Override
    public void updatePlaylist(Playlist playlist) throws Exception {

    }

    @Override
    public void deletePlaylist(Playlist playlist) throws Exception {

    }
}
