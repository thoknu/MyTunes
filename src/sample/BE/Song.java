package sample.BE;

public class Song {
    private String title;
    private String artist;
    private String category;
    private String filePath;
    private int minute;
    private int second;
    private int id;

    public Song(String title, String artist, String category, String filePath, int minute, int second, int id) {
        setTitle(title);
        setArtist(artist);
        setCategory(category);
        setFilePath(filePath);
        setId(id);
        Duration duration = new Duration(minute, second);
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

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
