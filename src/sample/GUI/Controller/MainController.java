package sample.GUI.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.BE.Playlist;
import sample.BE.Song;
import sample.BE.SongsInPlaylist;
import sample.GUI.Model.MainModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {

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
    private TableView<Song> tvSongs;
    @FXML
    private TableColumn<Song,String> colTitle;
    @FXML
    private TableColumn<Song,String> colArtist;
    @FXML
    private TableColumn<Song,String> colCategory;
    @FXML
    private TableColumn<Song,Integer> colTime;

    private final MainModel mainModel;

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
        //colTime.setCellValueFactory(new PropertyValueFactory<>("time"));


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
            for (int i = 0; i < songsInPlaylist.size(); i++) {
                SongsInPlaylist song = songsInPlaylist.get(i);
                String detail = (i + 1) + ": " + song.getTitle() + " - " + song.getArtist();
                songDetails.add(detail);
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
    public void onPreviousSong(ActionEvent actionEvent) {

    }

    public void onPlaySong(ActionEvent actionEvent) {

    }

    public void onNextSong(ActionEvent actionEvent) {

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

    public void onMoveSongUp(ActionEvent actionEvent) {

    }

    public void onMoveSongDown(ActionEvent actionEvent) {

    }

    public void onRemoveSongFromPlaylist(ActionEvent actionEvent) {

    }

    public void onNewSong(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewSong.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Import New Song");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onEditSong(ActionEvent actionEvent) {

    }

    public void onDeleteSong(ActionEvent actionEvent) {
        // Get the selected song from the lvSongsInPlaylist listview
        SongsInPlaylist selectedSong = (SongsInPlaylist) lvSongsInPlaylist.getSelectionModel().getSelectedItem();

        // Get the selected playlist from the tvPlaylists tableview
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong != null && selectedPlaylist != null) {
            int playlistID = selectedPlaylist.getId();
            int songID = selectedSong.getSongID();
            // Remove the selected song from the selected playlist
            mainModel.removeSongFromPlaylist(playlistID, songID);
        }
    }

    public void onClose(ActionEvent actionEvent) {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong != null && selectedPlaylist != null) {
            try {
                mainModel.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
                updateSongsInPlaylistView(selectedPlaylist); // Refresh the songs list view
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select both a song and a playlist.");
            alert.showAndWait();
        }
    }

    public void onAddSongToPlaylist(ActionEvent actionEvent) {
        Song selectedSong = tvSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tvPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong != null && selectedPlaylist != null) {
            mainModel.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            updateSongsInPlaylistView(selectedPlaylist);
        }
    }


}
