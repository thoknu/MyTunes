package sample.BE;

public class Song {
    private String title;
    private String artist;
    private String category;
    private String filePath;
    private int minute;
    private int seconds;
    private int id;

    public Song(String title, String artist, String category, String filePath,int seconds, int id) {
        setTitle(title);
        setArtist(artist);
        setCategory(category);
        setFilePath(filePath);
        setId(id);
        // need to get time from the controller in seconds.
    }

    @Override
    public String toString() {
        return title +": "
                + artist + ": "
                + category + ": "
                + filePath + ": "
                + seconds + ": "
                + id + ": ";
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
