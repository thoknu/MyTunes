package sample.GUI.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BE.SongsInPlaylist;
import sample.GUI.Model.MainModel;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


public class MainController {

    @FXML
    private Label lblCurrentSong;
    @FXML
    private Label lblVolume;
    @FXML
    private Slider sliderVolumeSlider;
    @FXML
    private TextField txtfFilterSearch;

    @FXML
    private ListView lvSongsInPlaylist;

    @FXML
    private TableView<Playlist> tvPlaylists;
    @FXML
    private TableColumn<Playlist,String> colName;
    @FXML
    private TableColumn<Playlist,String> colSongs;
    @FXML
    private TableColumn<Playlist,Integer> colPlaylistTime;

    @FXML
    private TableView<Song> tvSongs;
    @FXML
    private TableColumn<Song,String> colTitle;
    @FXML
    private TableColumn<Song,String> colArtist;
    @FXML
    private TableColumn<Song,String> colCategory;
    @FXML
    private TableColumn<Song,Integer> colTime;

    private Map<String, SongsInPlaylist> songsInPlaylistMap = new HashMap<>();
    private final MainModel mainModel;
    private MediaPlayer mediaPlayer;
    private Media media;
    private Song currentSong;
    private int currentPlaylistId;
    private boolean isPaused = false;

    public MainController() {
        try {
            mainModel = new MainModel();
        } catch (Exception e) {
            displayError(e);
            throw new IllegalStateException("Failed to initialize MainModel", e);
        }
    }

    /**
     * Sets up the UI components upon loading the FXML.
     */
    @FXML
    private void initialize() {
        setupSongTableView();
        setupPlaylistTableView();
        setupPlaylistSelectionListener();
        lblCurrentSong.setText("...Nothing is playing");
        setupVolumeSlider();

    }

    ///////////////////////////
    ///   Helper Methods    ///
    /// Construction & init ///
    ///////////////////////////

    /**
     * Configures the table view for displaying songs.
     */
    private void setupSongTableView() {
        tvSongs.setItems(mainModel.getObservableSongs());
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));
    }

    /**
     * Configures the table view for displaying playlists.
     */
    private void setupPlaylistTableView() {
        tvPlaylists.setItems(mainModel.getObservablePlaylists());
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSongs.setCellValueFactory(new PropertyValueFactory<>("songCount"));
        colPlaylistTime.setCellValueFactory(new PropertyValueFactory<>("formattedTotalTime"));
    }

    /**
     * Listens for playlist selection changes and updates the song list accordingly.
     */
    private void setupPlaylistSelectionListener() {
        tvPlaylists.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSongsInPlaylistView(newValue);
            }
        });
    }

    /**
     * Updates the list view to show songs in the selected playlist.
     *
     * @param selectedPlaylist The playlist selected by the user.
     */
    private void updateSongsInPlaylistView(Playlist selectedPlaylist) {
        try {
            List<SongsInPlaylist> songsInPlaylist = mainModel.getAllSongsInPlaylist(selectedPlaylist);
            List<String> songDetails = new ArrayList<>();
            songsInPlaylistMap.clear();

            for (SongsInPlaylist song : songsInPlaylist) {
                String detail = song.getTitle() + " - " + song.getArtist();
                songDetails.add(detail);
                songsInPlaylistMap.put(detail, song);
            }
            lvSongsInPlaylist.setItems(FXCollections.observableArrayList(songDetails));
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Initializes the volume slider.
     */
    private void setupVolumeSlider(){
        sliderVolumeSlider.valueProperty().addListener((Observable, oldValue, newValue) -> {
            if (mediaPlayer != null){
                mediaPlayer.setVolume(newValue.doubleValue());
            }
        });
    }

    ////////////////////////////
    //// Playlist Functions ////
    ////////////////////////////

    /**
     * Opens a new window for creating a new playlist.
     */
    @FXML
    private void onNewPlaylist(ActionEvent actionEvent) throws IOException {
        Stage stage = loadStage("/NewPlaylist.fxml", "Create New Playlist", null);
        stage.showAndWait();
    }

    /**
     * Opens the edit playlist window for the selected playlist.
     */
    @FXML
    private void onEditPlaylist(ActionEvent actionEvent) throws IOException {
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            Stage stage = loadStage("/NewPlaylist.fxml", "Edit Playlist", selectedPlaylist);
            stage.showAndWait();
        } else {
            showAlert("No playlist selected", "Please select a playlist to edit.");
        }
    }

    /**
     * Deletes the selected playlist after confirmation from the user.
     */
    @FXML
    private void onDeletePlaylist(ActionEvent actionEvent) {
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            boolean confirmDelete = showConfirmationAlert("Delete Playlist",
                    "Are you sure you want to delete the playlist: " + selectedPlaylist.getName() + "?");
            if (confirmDelete) {
                mainModel.deletePlaylist(selectedPlaylist);
            }
        }else {
            showAlert("No playlist selected", "Please select a playlist to delete.");
        }
    }

    /**
     * Adds the selected song to the selected playlist.
     */
    @FXML
    private void onAddSongToPlaylist(ActionEvent actionEvent) {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong != null && selectedPlaylist != null) {
            mainModel.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            updateSongsInPlaylistView(selectedPlaylist);
            tvPlaylists.refresh();
        } else {
            showAlert("Selection Missing", "Select both a song to add and a playlist to add it to.");
        }
    }

    /**
     * Removes the selected song from the selected playlist.
     */
    @FXML
    private void onRemoveSongFromPlaylist(ActionEvent actionEvent) {
        String selectedSongDetail = (String) lvSongsInPlaylist.getSelectionModel().getSelectedItem();

        if (selectedSongDetail != null && songsInPlaylistMap.containsKey(selectedSongDetail)) {
            try {
                SongsInPlaylist selectedSong = songsInPlaylistMap.get(selectedSongDetail);
                mainModel.removeSongFromPlaylist(selectedSong.getEntryID(), selectedSong.getPlaylistID());
                updateSongsInPlaylistViewUsingID(selectedSong.getPlaylistID());
                tvPlaylists.refresh();
            } catch (SQLException e) {
                displayError(e);
            }
        } else {
            showAlert("Selection Missing", "Select a song in the playlist to delete.");
        }
    }

    /**
     * Moves the selected song up in the playlist order.
     */
    @FXML
    private void onMoveSongUp(ActionEvent actionEvent) {
        int selectedSong = lvSongsInPlaylist.getSelectionModel().getSelectedIndex();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong > 0){
            try {
                mainModel.moveSongUp(selectedPlaylist.getId(), selectedSong);
                updateSongsInPlaylistView(selectedPlaylist);
            } catch (SQLException e){
                displayError(e);
            }
        }
    }

    /**
     * Moves the selected song down in the playlist order.
     */
    @FXML
    private void onMoveSongDown(ActionEvent actionEvent) {
        int selectedSongIndex = lvSongsInPlaylist.getSelectionModel().getSelectedIndex();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSongIndex < lvSongsInPlaylist.getItems().size() - 1) {
            try {
                mainModel.moveSongDown(selectedPlaylist.getId(), selectedSongIndex);
                updateSongsInPlaylistView(selectedPlaylist);
            } catch (SQLException e) {
                displayError(e);
            }
        }
    }

    ////////////////////////
    //// Helper Methods ////
    ////   Playlists    ////
    ////////////////////////



    /**
     * Updates the songs list in the playlist view based on a given playlist ID.
     *
     * @param playlistID The ID of the playlist whose songs are to be displayed.
     */
    private void updateSongsInPlaylistViewUsingID(int playlistID) {
        Playlist playlist = mainModel.getPlaylistByID(playlistID);
        updateSongsInPlaylistView(playlist);
    }


    ////////////////////////
    //// Song Functions ////
    ////////////////////////

    /**
     * Opens a new window to add a new song.
     */
    @FXML
    private void onNewSong(ActionEvent actionEvent) throws IOException {
        Stage stage = loadStage("/NewSong.fxml", "Import New Song", null);
        stage.show();
    }

    /**
     * Opens a new window to edit the selected song.
     */
    @FXML
    private void onEditSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            Stage stage = loadStage("/NewSong.fxml", "Edit Song", selectedSong);
            stage.show();
        } else {
            showAlert("No song selected", "Please select a song to edit");
        }
    }

    /**
     * Deletes the selected song after confirmation from the user.
     */
    @FXML
    private void onDeleteSong(ActionEvent actionEvent) {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            try {
                boolean confirmDelete = showConfirmationAlert("Delete Playlist",
                        "Are you sure you want to delete this song: " + selectedSong.getTitle() + "?");
                if (confirmDelete) {
                    mainModel.deleteSong(selectedSong);
                    tvPlaylists.refresh();
                }
            } catch (Exception e)
            {
                showAlert("Something went wrong", "Something went wrong while trying to delete, please try again");
            }
        }else {
            showAlert("Selection Required", "Please select a song to delete");
        }
    }


    ////////////////////////
    //// Helper Methods ////
    ////      Songs     ////
    ////////////////////////

    /**
     * Updates the TableView with a list of songs.
     *
     * @param songs The list of songs to display in the TableView.
     */
    private void updateTableView(List<Song> songs) {
        tvSongs.getItems().clear();
        tvSongs.getItems().addAll(songs);
    }


    ////////////////////////////////
    //// Media-player Functions ////
    ////////////////////////////////

    /**
     * This method handles the playback of songs,
     * including playing, pausing and resuming songs.
     */
    @FXML
    private void onPlaySong(ActionEvent actionEvent) throws Exception {
        // Fetch the selected playlist and song index
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();
        int selectedSong = lvSongsInPlaylist.getSelectionModel().getSelectedIndex();

        // Check if a new song or playlist is selected
        boolean isNewSelection = isNewSongOrPlaylistSelected(selectedPlaylist, selectedSong);

        // manage playback based on current status (play, pause, resume)
        if (mediaPlayer != null && !isNewSelection) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause(); // Pause if already playing
                isPaused = true;
            } else if (isPaused) {
                mediaPlayer.play(); // Resume if paused
                isPaused = false;
            }
        } else {
            // Play a new song if a new selection is made
            playNewSelection(selectedPlaylist, selectedSong);
        }
    }

    /**
     * Handles the action for playing the next song in the playlist.
     * Stops the current song and plays the next one.
     */
    @FXML
    private void onNextSong(ActionEvent actionEvent) throws Exception {
        if (currentSong != null) {
            Song nextSong = mainModel.getNextSong(currentPlaylistId, currentSong.getId());
            if (nextSong != null) {
                stopCurrentSong();
                playSong(nextSong, currentPlaylistId); // Pass currentPlaylistId
            }
        }
    }

    /**
     * Handles the action for playing the previous song in the playlist.
     * Stops the current song and plays the previous one.
     */
    @FXML
    private void onPreviousSong(ActionEvent actionEvent) throws Exception {
        if (currentSong != null) {
            Song prevSong = mainModel.getPreviousSong(currentPlaylistId, currentSong.getId());
            if (prevSong != null) {
                stopCurrentSong();
                playSong(prevSong, currentPlaylistId); // Pass currentPlaylistId
            }
        }
    }


    ////////////////////////
    //// Helper Methods ////
    ////  Media-player  ////
    ////////////////////////

    /**
     * Determines if a new song or playlist is selected.
     * @return true if a new selection is made; false otherwise.
     */
    private boolean isNewSongOrPlaylistSelected(Playlist selectedPlaylist, int selectedSongIndex) throws Exception {
        // Check for new playlist selection
        if (selectedPlaylist != null && selectedPlaylist.getId() != currentPlaylistId) {
            return true;
        }
        // Check for new song selection within the same playlist
        if (selectedSongIndex != -1 && currentSong != null) {
            SongsInPlaylist selectedSongInPlaylist = mainModel.getAllSongsInPlaylist(selectedPlaylist).get(selectedSongIndex);
            return selectedSongInPlaylist.getSongID() != currentSong.getId();
        }
        return false; // No new selection
    }

    /**
     * This method manages whether to play a selected song or playlist.
     * If a playlist is selected but no specific song is chosen, it plays the first song in the playlist.
     * If the playlist is empty, it shows an alert.
     *
     * @param selectedPlaylist The selected playlist.
     * @param selectedSong The index of the selected song in the playlist.
     */
    private void playNewSelection(Playlist selectedPlaylist, int selectedSong) throws Exception {
        if (selectedPlaylist != null) {
            if (selectedSong != -1) {
                // Stop any currently playing song
                stopCurrentSong();
                // Play the selected song
                playSelectedSong(selectedPlaylist, selectedSong);
            } else if (!mainModel.getAllSongsInPlaylist(selectedPlaylist).isEmpty()) {
                // If no specific song is selected but the playlist has songs, play the first song
                stopCurrentSong();
                playFirstSongInPlaylist(selectedPlaylist);
            } else {
                // Show an alert if the playlist is empty
                showAlert("Empty Playlist", "The selected playlist has no songs.");
            }
        } else {
            // Show an alert if no playlist is selected
            showAlert("No Playlist Selected", "Please select a playlist to play songs from.");
        }
    }

    /**
     * Stops the currently playing song, if any.
     */
    private void stopCurrentSong() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }
    }

    /**
     * Plays the song that is selected in the playlist.
     * It fetches the song information based on the selected index in the playlist and then plays the song.
     *
     * @param playlist The playlist from which the song is selected.
     * @param songIndex The index of the selected song in the playlist.
     * @throws Exception If an error occurs during the process.
     */
    private void playSelectedSong(Playlist playlist, int songIndex) throws Exception {
        SongsInPlaylist selectedSongInPlaylist = mainModel.getAllSongsInPlaylist(playlist).get(songIndex);
        Song songToPlay = mainModel.getSongByID(selectedSongInPlaylist.getSongID());
        playSong(songToPlay, playlist.getId()); // Plays the selected song and updates the current playlist ID.
    }

    /**
     * Plays the first song in the given playlist.
     * It retrieves the first song in the playlist and initiates playback.
     *
     * @param playlist The playlist from which the first song is to be played.
     * @throws Exception If an error occurs during the process.
     */
    private void playFirstSongInPlaylist(Playlist playlist) throws Exception {
        SongsInPlaylist firstSongInPlaylist = mainModel.getAllSongsInPlaylist(playlist).get(0);
        Song songToPlay = mainModel.getSongByID(firstSongInPlaylist.getSongID());
        playSong(songToPlay, playlist.getId()); // Plays the first song in the playlist and updates the current playlist ID.
    }

    /**
     * Plays a song based on the provided song object and playlist ID.
     * If the requested song is already playing, it handles playback (pause/resume).
     * If it's a new song, it initializes and starts playing the new song.
     *
     * @param song The song to be played.
     * @param playlistId The ID of the playlist containing the song.
     * @throws IOException If there's an issue with file I/O.
     */
    private void playSong(Song song, int playlistId) throws IOException {
        if (!validateSongFile(song)) {
            showAlert("File Not Found", "The file for the song '" + song.getTitle() + "' was not found.");
            return;
        }

        if (isCurrentSongPlaying(song)) {
            handleCurrentSongPlayback();
        } else {
            stopCurrentSong();
            startNewSongPlayback(song, playlistId);
        }
    }

    /**
     * Validates if the file for the song exists.
     *
     * @param song The song to be validated.
     * @return boolean Returns true if the file exists, otherwise false.
     */
    private boolean validateSongFile(Song song) {
        String filePath = "songs/" + song.getFilePath();
        File songFile = new File(filePath);
        return songFile.exists();
    }

    /**
     * Checks if the current song is the same as the requested song to play.
     *
     * @param song The song to be checked.
     * @return boolean Returns true if the current song is the same as the requested song, otherwise false.
     */
    private boolean isCurrentSongPlaying(Song song) {
        return mediaPlayer != null && currentSong != null && currentSong.getId() == song.getId();
    }

    /**
     * Handles the playback of the current song. If it's playing, it pauses, and if it's paused, it resumes.
     */
    private void handleCurrentSongPlayback() {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            isPaused = true;
        } else if (isPaused) {
            mediaPlayer.play();
            isPaused = false;
        }
    }

    /**
     * Initializes and starts playback of a new song.
     *
     * @param song The song to be played.
     * @param playlistId The ID of the playlist containing the song.
     */
    private void startNewSongPlayback(Song song, int playlistId) {
        media = new Media(new File("songs/" + song.getFilePath()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        updateGuiWithCurrentSong(song, playlistId);
    }

    /**
     * Updates the GUI with the currently playing song's information.
     *
     * @param song The song that is currently playing.
     * @param playlistId The ID of the playlist containing the currently playing song.
     */
    private void updateGuiWithCurrentSong(Song song, int playlistId) {
        lblCurrentSong.setText(song.getTitle() + " by " + song.getArtist() + "  is playing");
        currentSong = song;
        currentPlaylistId = playlistId;
        isPaused = false;
    }


    ////////////////////////
    //// Misc Functions ////
    ////////////////////////

    /**
     * Filters the songs in the TableView based on the text entered in the search field.
     */
    @FXML
    private void onFilterSearch(ActionEvent actionEvent) {
        String searchText = txtfFilterSearch.getText();
        if (!searchText.isEmpty()) {
            try {
                List<Song> searchResult = mainModel.searchSongs(searchText);
                updateTableView(searchResult);
            } catch (Exception e) {
                displayError(e);
            }
        } else {
            // If the search field is empty, show all songs
            try {
                mainModel.refreshSongs();
            } catch (Exception e) {
                displayError(e);
            }
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    private void onClose(ActionEvent actionEvent) {
        Platform.exit();
    }

    ////////////////////////
    //// Helper Methods ////
    ////    General     ////
    ////////////////////////

    /**
     * Shows an alert dialog displaying an error.
     */
    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText("An error occurred: " + t.getMessage());
        alert.setContentText("Please try again or contact support if the problem persists.");
        alert.showAndWait();
    }

    /**
     * Shows an alert dialog with the given title and content.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation alert with the specified title and content.
     *
     * @param title   The title of the alert dialog.
     * @param content The content message displayed in the alert dialog.
     * @return boolean Returns true if the user clicks 'Yes', and false if the user clicks 'No' or closes the dialog.
     */
    private boolean showConfirmationAlert(String title, String content) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle(title);
        Optional<ButtonType> result = confirmAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    /**
     * Loads a new stage (window) with the specified FXML file and title.
     *
     * @param fxmlPath The path to the FXML file.
     * @param title The title of the new stage.
     * @param entity An optional entity (Playlist or Song) to pass to the controller.
     * @return Stage The newly created stage.
     * @throws IOException If there is an error loading the FXML file.
     */
    private Stage loadStage(String fxmlPath, String title, Object entity) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        configureController(loader.getController(), entity);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        return stage;
    }

    /**
     * Configures the controller with necessary data based on its type.
     * It checks the type of the controller and configures it accordingly.
     *
     * @param controller The controller to be configured.
     * @param entity The entity to be passed to the controller, if applicable.
     */
    private void configureController(Object controller, Object entity) {
        // Configure NewPlaylistController instances
        if (controller instanceof NewPlaylistController) {
            NewPlaylistController playlistController = (NewPlaylistController) controller;
            // If the entity is a Playlist, set it in the controller for editing
            if (entity instanceof Playlist) {
                playlistController.setEditingPlaylist((Playlist) entity);
            }
        }
        // Configure NewSongController instances
        else if (controller instanceof NewSongController) {
            NewSongController songController = (NewSongController) controller;
            songController.setMainModel(mainModel);
            // If the entity is a Song, set it in the controller for editing
            if (entity instanceof Song) {
                songController.setUpdatedSong((Song) entity);
            }
        }
    }


}
