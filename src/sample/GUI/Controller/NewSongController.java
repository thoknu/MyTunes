package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
    }

    public void onCancelSong(ActionEvent actionEvent) {
    }
}
