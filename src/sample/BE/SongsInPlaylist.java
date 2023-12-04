package sample.BE;

public class SongsInPlaylist {
    private final int songID;
    private final int playlistID;
    private String title;
    private String source;

    public SongsInPlaylist(int playlistID, int songID, String title, String source) {
        this.playlistID = playlistID;
        this.songID = songID;
        this.title = title;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format("SongsInPlaylist{songID=%d, playlistID=%d, title='%s', source='%s'}", songID, playlistID, title, source);
    }
}
