package sample.BE;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private List<Song> songs;
    private int hour;
    private int minute;
    private int second;
    private int id;

    public Playlist(String name, int hour, int minute, int second, int id) {
        setName(name);
        setId(id);
        songs = new ArrayList<>();
        Duration duration = new Duration(hour, minute, second);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Song> getAllSongs() {
        return songs;
    }

    public Song getSong(int id) {
        for (Song s : songs) {
            if (s.getId() == id) {
                return s;
            }
        }
        return new Song("", "", "", "", -1, -1,  -1);
    }
}
