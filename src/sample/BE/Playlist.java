package sample.BE;

import sample.BLL.PlaylistManager;

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
        setHour(hour);
        setMinute(minute);
        setSecond(second);
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
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
