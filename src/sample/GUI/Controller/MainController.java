package sample.GUI.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BE.SongsInPlaylist;
import sample.GUI.Model.MainModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class MainController {

    public Label lblCurrentSong;
    @FXML
    private Button lblPlayButton;
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
    public TableView<Song> tvSongs;
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
    private String isPlayingString = "... Is Playing";

    public MainController() {
        try {
            mainModel = new MainModel();
        } catch (Exception e) {
            displayError(e);
            throw new IllegalStateException("Failed to initialize MainModel", e);
        }
    }

    @FXML
    public void initialize() {
        setupTableView();
        setupPlaylistSelectionListener();
    }


    private void setupTableView() {
        tvSongs.setItems(mainModel.getObservableSongs());
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));


        tvPlaylists.setItems(mainModel.getObservablePlaylists());
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSongs.setCellValueFactory(new PropertyValueFactory<>("songCount"));
        colPlaylistTime.setCellValueFactory(new PropertyValueFactory<>("formattedTotalTime"));
    }

    private void setupPlaylistSelectionListener() {
        tvPlaylists.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSongsInPlaylistView(newValue);
            }
        });
    }

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
            e.printStackTrace();
        }
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("-You are stupid-");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
    public void onPreviousSong(ActionEvent actionEvent) throws IOException {
        mainModel.previousSong(true);
        lblCurrentSong.setText(mainModel.previousSong(false) + isPlayingString);
    }

    public void onPlaySong(ActionEvent actionEvent) throws IOException {
        mainModel.tempValueForSong(lvSongsInPlaylist.getSelectionModel().getSelectedIndex(), true);
        mediaPlayer = mainModel.getMediaPlayer();
        mediaPlayer.setVolume(sliderVolumeSlider.getValue() * 0.01);
        lblCurrentSong.setText(mainModel.playPauseSong(lvSongsInPlaylist.getSelectionModel().getSelectedIndex(), false) + isPlayingString);
    }

    public void onNextSong(ActionEvent actionEvent) throws IOException {
        mainModel.nextSong(true);
        lblCurrentSong.setText(mainModel.nextSong(false) + isPlayingString);
    }

    public void onFilterSearch(ActionEvent actionEvent) {

    }

    public void onNewPlaylist(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewPlaylist.fxml"));
        Parent root = loader.load();

        NewPlaylistController controller = loader.getController();
        controller.setMainModel(mainModel);

        Stage stage = new Stage();
        stage.setTitle("Create New Playlist");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void onEditPlaylist(ActionEvent actionEvent) throws IOException {
        // Get the selected playlist from the tableview
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewPlaylist.fxml"));
            Parent root = loader.load();
            NewPlaylistController controller = loader.getController();
            controller.setMainModel(mainModel);
            controller.setEditingPlaylist(selectedPlaylist);

            Stage stage = new Stage();
            stage.setTitle("Edit playlist");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void onDeletePlaylist(ActionEvent actionEvent) {
        // Get the selected playlist from the tableview
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the playlist: " + selectedPlaylist.getName() + "?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait();

            if (confirmAlert.getResult() == ButtonType.YES) {
                mainModel.deletePlaylist(selectedPlaylist);
            }
        }
    }

    public void onNewSong(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewSong.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Import New Song");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onEditSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewSong.fxml"));
            Parent root = loader.load();

            NewSongController controller = loader.getController();
            controller.setMainModel(mainModel);
            controller.setUpdatedSong(selectedSong);


            Stage stage = new Stage();
            stage.setTitle("Edit Song");
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please select a song to edit");
            alert.showAndWait();
        }

    }

    public void onDeleteSong(ActionEvent actionEvent) {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            try {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete this song: " + selectedSong.getTitle() + "?",
                        ButtonType.YES, ButtonType.NO);
                confirmAlert.showAndWait();
                if (confirmAlert.getResult() == ButtonType.YES){
                    mainModel.deleteSong(selectedSong);
                }
            } catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Something went wrong while trying to delete, please try again");
                alert.showAndWait();
            }
        }
    }

    public void onClose(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onAddSongToPlaylist(ActionEvent actionEvent) {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong != null && selectedPlaylist != null) {
            mainModel.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            updateSongsInPlaylistView(selectedPlaylist);
            tvPlaylists.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select both a song to add and a playlist to add it to.");
            alert.showAndWait();
        }
    }

    public void onRemoveSongFromPlaylist(ActionEvent actionEvent) throws SQLException {
        String selectedSongDetail = (String) lvSongsInPlaylist.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null && selectedSongDetail != null && songsInPlaylistMap.containsKey(selectedSongDetail)) {
            SongsInPlaylist selectedSong = songsInPlaylistMap.get(selectedSongDetail);
            mainModel.removeSongFromPlaylist(selectedSong.getEntryID(), selectedSong.getPlaylistID());
            updateSongsInPlaylistView(selectedPlaylist);
            tvPlaylists.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a song in the playlist to delete.");
            alert.showAndWait();
        }
    }

    public void onMoveSongUp(ActionEvent actionEvent) {
        int selectedSong = lvSongsInPlaylist.getSelectionModel().getSelectedIndex();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong > 0){
            try {
                mainModel.moveSongUp(selectedPlaylist.getId(), selectedSong);
                // Refresh the playlist view or adjust the GUI accordingly
                updateSongsInPlaylistView(selectedPlaylist);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void onMoveSongDown(ActionEvent actionEvent) {
        int selectedSong = lvSongsInPlaylist.getSelectionModel().getSelectedIndex();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();


        if(selectedSong < lvSongsInPlaylist.getItems().size() - 1){
            try {
                mainModel.moveSongDown(selectedPlaylist.getId(), selectedSong);
                // Refresh the playlist view or adjust the GUI accordingly
                updateSongsInPlaylistView(selectedPlaylist);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }


}
