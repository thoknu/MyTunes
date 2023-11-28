package sample.GUI.Controller;

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


    private MainModel mainModel;

    public MainController() {
        try {
            mainModel = new MainModel();
        } catch (Exception e) {
            System.err.println("Error creating MainModel: " + e.getMessage());
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

        // Initialize the table with the four columns.
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Add data from observable list
        tvSongs.setItems(mainModel.getObservableSongs());

        tvPlaylists.setItems(mainModel.getObservablePlaylists());
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSongs.setCellValueFactory(new PropertyValueFactory<>("songs"));
        colPlaylistTime.setCellValueFactory(new PropertyValueFactory<>("time"));

    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("-You are stupid-");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
}
