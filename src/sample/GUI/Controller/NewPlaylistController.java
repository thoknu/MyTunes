package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.BE.Playlist;
import sample.GUI.Model.MainModel;

import java.net.URL;
import java.util.ResourceBundle;

public class NewPlaylistController{
    @FXML
    private TextField txtfName;
    private MainModel mainModel;

    public NewPlaylistController(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public void onSavePlaylist(ActionEvent actionEvent) {
        String name = txtfName.getText().trim();
        if (!name.isEmpty()){
            Playlist newPlaylist = mainModel.createPlaylist(name);
            if (newPlaylist != null){
                closeStage();
            } else {
                //handle playlist creation failue
            }
        } else {
            //handle empty playlist name
            displayError("Playlist name cannot be empty.");
        }
    }



    public void onCancelPlaylist(ActionEvent actionEvent) {
        closeStage();
    }

    private void displayError(String s) {
        // Implementation of error message display
    }

    private void closeStage() {
        Stage stage = (Stage) txtfName.getScene().getWindow();
        stage.close();
    }

    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

}
