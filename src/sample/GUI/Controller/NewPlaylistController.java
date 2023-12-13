package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    private Playlist editingPlaylist;

    public void setEditingPlaylist(Playlist editingPlaylist){
        this.editingPlaylist = editingPlaylist;
        if (editingPlaylist != null){
            txtfName.setText(editingPlaylist.getName());
        }
    }

    public void onSavePlaylist(ActionEvent actionEvent) {
        String playlistName = txtfName.getText();
        if (!playlistName.isEmpty()) {
            if (editingPlaylist == null) {
            try { // Creating a new playlist
                    Playlist newPlaylist = mainModel.createPlaylist(playlistName);
                    mainModel.getObservablePlaylists().add(newPlaylist);
            } catch (Exception e) {
                e.printStackTrace();}
            } else { // Updating an existing playlist
                    editingPlaylist.setName(playlistName);
                    mainModel.editPlaylist(editingPlaylist);
                }
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
        }

    }

    public void onCancelPlaylist(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }


}
