package sample.BE;

public class SongsInPlaylist {
    private final int songID;
    private final int playlistID;
    private String title;
    private String artist;

    public SongsInPlaylist(int playlistID, int songID, String title, String artist) {
        this.playlistID = playlistID;
        this.songID = songID;
        this.title = title;
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getSongID() {
        return songID;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("SongsInPlaylist{songID=%d, playlistID=%d, title='%s', artist='%s'}", songID, playlistID, title, artist);
    }
}
