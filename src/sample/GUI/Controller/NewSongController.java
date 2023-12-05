package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.BE.Song;
import sample.GUI.Model.NewSongModel;

public class NewSongController {
    @FXML
    private TextField txtfTitle;
    @FXML
    private TextField txtfArtist;
    @FXML
    private ComboBox cbCategory;
    @FXML
    private TextField txtfTime;
    @FXML
    private TextField txtfFilePath;
    private NewSongModel newSongModel;

    public void onSaveSong(ActionEvent actionEvent) {
        String title = txtfTitle.getText();
        String artist = txtfArtist.getText();
        String category = (String) cbCategory.getValue();  // Skal lige testes, er cast til at være en String og ikke object.
        String time = txtfTime.getText();
        String filePath = txtfFilePath.getText();

        try {

            int durationInSeconds = newSongModel.calculateSecondsFromUserInput(time);
            Song newSong = new Song(title, artist, category,filePath,durationInSeconds,0);


            newSongModel.createNewSong(newSong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void onCancelSong(ActionEvent actionEvent) {
        Stage stage = (Stage) txtfTitle.getScene().getWindow();
        stage.close();
    }

}
