package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.GUI.Model.MainModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

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
    private TableView tvPlaylists;
    @FXML
    private TableView tvSongs;
    private MainModel mainModel;

    public MainController() {

        try {
            mainModel = new MainModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
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
        Stage stage = new Stage();
        stage.setTitle("Create New Playlist");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onEditPlaylist(ActionEvent actionEvent) {

    }

    public void onDeletePlaylist(ActionEvent actionEvent) {

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

    }

    public void onClose(ActionEvent actionEvent) {

    }

    public void onMoveSong(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tvSongs.setItems(mainModel.getObservableSongs());
        tvPlaylists.setItems(mainModel.getObservablePlaylists());
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("-You are stupid-");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
}
