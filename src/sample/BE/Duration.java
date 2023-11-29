package sample.BE;

public class Duration {
    private int hour, minute, second;

    public Duration(int hour, int minute, int second) {

    }

    public Duration(int minute, int second) {

    }

    public Duration (int second){
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

    public int getDurationInSecondsSong(int minute, int second) {
        int seconds = 0;
        seconds += minute * 60;
        return seconds + second;
    }

    public int getDurationInSecondsPlayList(int hour, int minute, int second) {
        int seconds = 0;
        seconds += hour * 3600;
        seconds += minute * 60;
        return seconds + second;
    }
}
