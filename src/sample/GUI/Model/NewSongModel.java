package sample.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import sample.BE.Song;
import sample.BLL.SongManager;

import java.sql.SQLException;


public class NewSongModel {
    private SongManager songManager;
    private ObservableList<Song> songsToBeViewed = FXCollections.observableArrayList();


    public NewSongModel(){
        try {
            songManager = new SongManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Method for creating new song object and sending down layers.
     *  Also adds the song to the list of songs.
     *
     * @param newSong The song that needs to be created.
     * @throws SQLException If the song cannot be created.
     */
    public void createNewSong(Song newSong) throws SQLException {
        Song song = songManager.createNew0Song(newSong);
        songsToBeViewed.add(song);
    }

    /**
     * Method for updating song, much like creating a song.
     *
     * @param updatedSong The new song that replaces the previous one.
     * @throws SQLException If the song cannot be updated.
     */
    public void updateSong(Song updatedSong) throws SQLException {
        songManager.updateSong(updatedSong);

        int index = songsToBeViewed.indexOf(updatedSong);

        if (index >= 0 && index < songsToBeViewed.size()) {
            Song s = songsToBeViewed.get(index);

            // Update observable list and UI
            s.setTitle(updatedSong.getTitle());
            s.setArtist(updatedSong.getArtist());
            s.setCategory(updatedSong.getCategory());
            s.setFilePath(updatedSong.getFilePath());
            s.setSeconds(updatedSong.getSeconds());
        }
    }

    /**
     * Method for calculating the user input for time.
     * Takes the validated input from method:userInputValidation
     * splits the user input into parts for a String array which then determines
     * how the time should be calculated depending on how many timeparts there are.
     *
     * @param timeFromUser Input amount of time from the user.
     * returns either the calculated time in seconds, or a -1 to show it was invalid input.
     */
    public int calculateSecondsFromUserInput(String timeFromUser)
    {
        try {
            if (!userInputValidation(timeFromUser)) {
                throw new IllegalArgumentException("Invalid time format: " + timeFromUser);
            }
            else
            {
                int hours = 0;
                int minutes = 0;
                int seconds = 0;

                // Valid ways to split the time from the user.
                String[] timeParts = timeFromUser.split("[\\s:,;.-]+");

                switch (timeParts.length) {
                    case 3:
                        hours = Integer.parseInt(timeParts[0]);
                        minutes = Integer.parseInt(timeParts[1]);
                        seconds = Integer.parseInt(timeParts[2]);
                        break;
                    case 2:
                        minutes = Integer.parseInt(timeParts[0]);
                        seconds = Integer.parseInt(timeParts[1]);
                        break;
                    case 1:
                        seconds = Integer.parseInt(timeParts[0]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid time format: " + timeFromUser);
                }

                return hours * 3600 + minutes * 60 + seconds;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Method to validate the timeFromUser.
     *
     * @param timeFromUser The user input time.
     * @return true or false depending on if the time is in a valid format.
     */
    private boolean userInputValidation(String timeFromUser)
    {
        // input cant be empty
        if (timeFromUser == null || timeFromUser.trim().isEmpty()) {
            return false;
        }

        String[] timeParts = timeFromUser.split("[\\s:,;.-]+");
        // input cant be split into more than 3 parts
        if (timeParts.length >= 4) {
            return false;
        }
        // input cant be letters.
        for (String part : timeParts) {
            if (!part.matches("^[0-9]+$")) {
                    return false;
            }
        }
        return true;
    }

}
