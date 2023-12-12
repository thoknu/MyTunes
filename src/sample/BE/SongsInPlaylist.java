package sample.BE;

public class SongsInPlaylist {
    private final int entryID;
    private final int songID;
    private final int playlistID;
    private String title;
    private String artist;
    private int order;

    public SongsInPlaylist(int entryID, int playlistID, int songID, String title, String artist) {
        this.entryID = entryID;
        this.playlistID = playlistID;
        this.songID = songID;
        this.title = title;
        this.artist = artist;
    }

    public int getEntryID() {
        return entryID;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    public int getSongID() {
        return songID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return String.format("SongsInPlaylist{songID=%d, playlistID=%d, title='%s', artist='%s'}", songID, playlistID, title, artist);
    }
}
