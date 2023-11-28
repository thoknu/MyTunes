package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
        // newSongModel.createSong(txtfTitle.getText(), txtfArtist.getText(), cbCategory.getValue().toString(), txtfTime.getText(), txtfFilePath.getText());
        String title = txtfTitle.getText();
        String artist = txtfArtist.getText();
        String category = (String) cbCategory.getValue();  // Skal lige testes, er cast til at være en String og ikke object.
        String time = txtfTime.getText();
        String filePath = txtfFilePath.getText();

        // Validate inputs (you might want to show an error message to the user if any of the fields are empty)

        int durationInSeconds = newSongModel.calculateSecondsFromUserInput(time);

        // Pass the converted value down to the next layer (NewSongModel or SongManager)
        newSongModel.createSong(title, artist, category, durationInSeconds, filePath);

    }

    public void onCancelSong(ActionEvent actionEvent) {
    }

}
