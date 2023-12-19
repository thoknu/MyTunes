package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.BE.Playlist;
import sample.GUI.Model.MainModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewPlaylistController{
    @FXML
    private TextField txtfName;
    private MainModel mainModel;
    private Playlist editingPlaylist;

    /**
     * Sets the playlist to be edited in this controller.
     *
     * @param editingPlaylist The playlist to be edited; null if creating a new playlist.
     */
    public void setEditingPlaylist(Playlist editingPlaylist){
        this.editingPlaylist = editingPlaylist;
        if (editingPlaylist != null){
            txtfName.setText(editingPlaylist.getName());
        }
    }

    /**
     * Handles the save action for both creating a new playlist and updating an existing one.
     */
    public void onSavePlaylist(ActionEvent actionEvent) {
        String playlistName = txtfName.getText();
        if (!playlistName.isEmpty()) {
            if (editingPlaylist == null) {
            try { // Creating a new playlist
                    Playlist newPlaylist = mainModel.createPlaylist(playlistName);
                    mainModel.getObservablePlaylists().add(newPlaylist);
            } catch (Exception e) {
                displayError("A problem occured", "A problem occured trying to save the playlist");}
            } else { // Updating an existing playlist
                    editingPlaylist.setName(playlistName);
                    mainModel.editPlaylist(editingPlaylist);
                }
            closeStage(actionEvent);
        }

    }

    /**
     * Closes the current window without saving any changes.
     *
     * @param actionEvent The event that triggered this action.
     */
    public void onCancelPlaylist(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    /**
     * Sets the main model for this controller.
     *
     * @param mainModel The main model instance.
     */
    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Helper method to close the current stage.
     *
     * @param actionEvent The event from which to retrieve the stage to be closed.
     */
    public void closeStage(ActionEvent actionEvent){
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Shows an alert dialog displaying an error.
     */
    private void displayError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
