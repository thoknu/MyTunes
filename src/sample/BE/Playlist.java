package sample.BE;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Playlist {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleIntegerProperty songCount = new SimpleIntegerProperty();
    private SimpleIntegerProperty totalTimeInSeconds = new SimpleIntegerProperty();
    private SimpleStringProperty formattedTotalTime = new SimpleStringProperty();

    /**
     * Sets the information of the playlist.
     *
     * @param id The unique ID of the playlist.
     * @param name The name given to the playlist.
     * @param songCount The amount of songs in the playlist.
     * @param totalTimeInSeconds The duration of the playlist.
     */
    public Playlist(int id, String name, int songCount, int totalTimeInSeconds) {
        setId(id);
        setName(name);
        setSongCount(songCount);
        setTotalTime(totalTimeInSeconds);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getSongCount() {
        return songCount.get();
    }

    public void setSongCount(int songCount) {
        this.songCount.set(songCount);
    }

    public SimpleIntegerProperty songCountProperty() {
        return songCount;
    }

    public int getTotalTime() {
        return totalTimeInSeconds.get();
    }

    public void setTotalTime(int totalTime) {
        this.totalTimeInSeconds.set(totalTime);
        setFormattedTotalTime(formatTime(totalTime));
    }

    public SimpleIntegerProperty totalTimeProperty() {
        return totalTimeInSeconds;
    }

    public String getFormattedTotalTime() {
        return formattedTotalTime.get();
    }

    public void setFormattedTotalTime(String formattedTime) {
        this.formattedTotalTime.set(formattedTime);
    }

    public SimpleStringProperty formattedTotalTimeProperty() {
        return formattedTotalTime;
    }

    private String formatTime(int totalSecs) {
            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String toString() {
        return String.format("Playlist{id=%d, name='%s'}", getId(), getName());
    }
}