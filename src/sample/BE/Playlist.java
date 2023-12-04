package sample.BE;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Playlist {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleIntegerProperty songCount = new SimpleIntegerProperty();

    public Playlist(int id, String name, int songCount) {
        setId(id);
        setName(name);
        this.songCount.set(songCount);
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

    @Override
    public String toString() {
        return String.format("Playlist{id=%d, name='%s'}", getId(), getName());
    }
}