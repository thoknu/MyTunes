package sample.BE;

public class SongsInPlaylist {
    private final int entryID;
    private final int songID;
    private final int playlistID;
    private String title;
    private String artist;

    /**
     * Sets the values of the song in the playlist.
     *
     * @param entryID The ID of the entry into the playlist into the database.
     * @param playlistID The ID of the playlist that the song is in.
     * @param songID The ID of the selected song.
     * @param title The title of the song.
     * @param artist The artist who made the song.
     */
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

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return String.format("SongsInPlaylist{songID=%d, playlistID=%d, title='%s', artist='%s'}", songID, playlistID, title, artist);
    }
}
