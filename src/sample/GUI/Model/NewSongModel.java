package sample.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.BE.Song;
import sample.BLL.SongManager;
import sample.DAL.SongDAO;

import java.util.List;

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

    public void createNewSong(Song newSong) throws Exception {
        Song song = songManager.createNewSong(newSong);
        songsToBeViewed.add(song);
    }
    public ObservableList<Song> getSongsToBeViewed() {
        return songsToBeViewed;
    }
    public void searchSong (String query) throws Exception{
        List<Song> searchResults = songManager.searchSongs(query);
        songsToBeViewed.clear();
        songsToBeViewed.addAll(searchResults);
    }

    public void updateSong(Song updatedSong) throws Exception{
        songManager.updateSong(updatedSong);
        // update song in DAL, through all the layers.
        Song s = songsToBeViewed.get(songsToBeViewed.indexOf(updatedSong));

        // Update observable list and UI
        s.setTitle(updatedSong.getTitle());
        s.setArtist(updatedSong.getArtist());
        s.setCategory(updatedSong.getCategory());
        s.setFilePath(updatedSong.getFilePath());
        s.setSeconds(updatedSong.getSeconds());

    }

    public void deleteSong(Song selectedSong)throws Exception {
        // Deletes song in DAL
        songManager.deleteSong(selectedSong);
        // removes it from observable list and UI.
        songsToBeViewed.remove(selectedSong);

    }


    public int calculateSecondsFromUserInput(String timeFromUser) {
        String[] timeParts = timeFromUser.split("[\\s:,;.-]+");
        // checks is numerical
        if (timeParts.length != 2 && timeParts.length != 3) {
            throw new IllegalArgumentException("Invalid time format: " + timeFromUser);
        }
        // decides how many parts, so i can be split accordingly
        int hours = (timeParts.length == 3) ? Integer.parseInt(timeParts[0]) : 0;
        int minutes = Integer.parseInt(timeParts[timeParts.length - 2]);
        int seconds = Integer.parseInt(timeParts[timeParts.length - 1]);
        // checks the time isn't 61 minutes etc.
        validateTimeValues(hours, minutes, seconds);

        return hours * 3600 + minutes * 60 + seconds;
    }

    private boolean isValidTimeFormat(String[] timeParts) {
        return (timeParts.length == 2 && isNumeric(timeParts[0]) && isNumeric(timeParts[1])) ||
                (timeParts.length == 3 && isNumeric(timeParts[0]) && isNumeric(timeParts[1]) && isNumeric(timeParts[2]));
    }

    private void validateTimeValues(int hours, int minutes, int seconds) {
        if (minutes < 0 || seconds < 0 || seconds >= 60 || minutes >= 60 || hours < 0) {
            throw new IllegalArgumentException("Invalid time values.");
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");  // This Regex, checks for 0-9
    }
}
