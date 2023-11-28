package sample.GUI.Model;

import sample.BE.Song;
import sample.DAL.SongDAO;

public class NewSongModel {
    private SongDAO songDAO;

    public void createSong(String title, String artist, String category, int time, String filePath) {
        // Song song = new Song(title, artist, category, time, filePath)
    }
    public int calculateSecondsFromUserInput(String timeFromUser){
        int calculatedSeconds = 0;
        String[] timeParts = timeFromUser.split("[\\s:,;.-]+");

        if (timeParts.length < 2 || timeParts.length > 3) {
            // Handle invalid input format, throw an exception, or provide a default value
            throw new IllegalArgumentException("Invalid time format: " + timeFromUser);
        }

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if (timeParts.length == 2) {
            // Format: Minutes:Seconds
            minutes = Integer.parseInt(timeParts[0]);
            seconds = Integer.parseInt(timeParts[1]);
        } else {
            // Format: Hours:Minutes:Seconds
            hours = Integer.parseInt(timeParts[0]);
            minutes = Integer.parseInt(timeParts[1]);
            seconds = Integer.parseInt(timeParts[2]);
        }

        // Validating the values, so you cant have 61 seconds etc.
        if (minutes < 0 || seconds < 0 || seconds >= 60 || minutes >= 60 || hours < 0) {
            // Handle invalid values, throw an exception, or provide default values
            throw new IllegalArgumentException("Invalid time values: " + timeFromUser);
        }

        // total time conversion in seconds.
        return calculatedSeconds = hours * 3600 + minutes * 60 + seconds;
    }
}
