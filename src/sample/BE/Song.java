package sample.BE;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Song {
    private String title;
    private String artist;
    private String category;
    private String filePath;
    private int minute;
    private int seconds;
    private int id;
    private StringProperty formattedTime;

    /**
     * Sets the information of the song.
     *
     * @param title The title of the song.
     * @param artist The artist who made the song.
     * @param category The category that the song is in.
     * @param filePath The name of the file.
     * @param seconds The duration of the song in seconds.
     * @param id The ID of the song.
     */
    public Song(String title, String artist, String category, String filePath, int seconds, int id) {
        setTitle(title);
        setArtist(artist);
        setCategory(category);
        setFilePath(filePath);
        setId(id);

        // Converts the time to a readable format ei. 2:21
        this.formattedTime = new SimpleStringProperty(formattedTime());
        setSeconds(seconds);
    }
    public String formattedTime() {
        int hours = getSeconds() / 3600;
        int minutes = (getSeconds() % 3600) / 60;
        int seconds = getSeconds() % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    public StringProperty formattedTimeProperty() {
        return formattedTime;
    }

    public String getFormattedTime() {
        return formattedTime.get();
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

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
        this.formattedTime.set(formattedTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
