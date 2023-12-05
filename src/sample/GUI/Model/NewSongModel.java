package sample.GUI.Model;

import javafx.collections.ObservableList;
import sample.BE.Song;
import sample.BLL.SongManager;
import sample.DAL.SongDAO;

import java.util.List;

public class NewSongModel {
    private SongManager songManager;

    private ObservableList<Song> songsToBeViewed;

    public void createNewSong(Song newSong) throws Exception {
        Song song = songManager.createNewSong(newSong);
        songsToBeViewed.add(song);
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


    public int calculateSecondsFromUserInput(String timeFromUser){
        int calculatedSeconds = 0;
        String[] timeParts = timeFromUser.split("[\\s:,;.-]+");

        if (timeParts.length < 2 || timeParts.length > 3) {
            // Handle invalid input format
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
