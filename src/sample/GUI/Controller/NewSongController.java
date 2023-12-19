package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import sample.BE.Song;
import sample.GUI.Model.MainModel;
import sample.GUI.Model.NewSongModel;

import java.sql.SQLException;
import java.util.Optional;

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
    private MainModel mainModel;
    private Song selecetedSong;

    /**
     * Initializes the model and category combo box.
     *
     * @throws Exception
     */
    @FXML
    public void initialize() throws Exception {
        newSongModel = new NewSongModel();
        setupCategoryComboBox();
    }

    /**
     * Ensures it's the correct mainModel, so it isn't null when called.
     *
     * @param mainModel
     */
    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    /**
     * Saves the new or edited song when the save button is clicked.
     *
     * @param actionEvent
     */

    public void onSaveSong(ActionEvent actionEvent) {

        if (selecetedSong == null) {
            handleNewSong();
        } else {
            handleSongEdit();
        }
        closeWindow();
    }

    /**
     * Handles creating a new song and adding it to the database.
     */
    private void handleNewSong() {
        Song newSong = getUserInput();

        if (newSong != null) {
            try {
                newSongModel.createNewSong(newSong);
                showAlert("Song Added", "The song has been successfully added.");
                mainModel.refreshSongs();
            } catch (SQLException e) {
                displayError("Error", "An error occurred while adding the song.", e);
            }
        }
    }

    /**
     * Handles updating an existing song with new information.
     */
    private void handleSongEdit() {
        Song updatedSong = getUserInput();

        if (updatedSong != null && selecetedSong != null) {
            updateExistingSong(updatedSong);

            try {
                    newSongModel.updateSong(selecetedSong);
                showAlert("Song Updated", "The song has been successfully updated.");
                mainModel.refreshSongs();
            } catch (SQLException e) {
                displayError("Error", "An error occurred while updating the song.", e);
            }
        }
    }

    /**
     * Closes the song dialog window.
     *
     * @param actionEvent
     */
    public void onCancelSong(ActionEvent actionEvent) {
        closeWindow();
    }

    ////////////////////////
    //// Helper Methods ////
    ////////////////////////

    /**
     * Code for setting up the category combo box
     */
    private void setupCategoryComboBox(){
        cbCategory.getItems().clear();
        cbCategory.getItems().addAll("Blues","Classical","Country","Electronic","Funk","Instrumental","Jazz","Latin","Metal","Pop","Punk","R&B","Rap","Reggae","Rock","Techno","Miscellaneous");
        cbCategory.getSelectionModel().select("Pop");
    }

    /**
     * Method to handle user input
     * used to get the content of the different txtfields in the GUI
     * also calculates the time input, and checks if its valid.
     *
     * @return The song object with the input details.
     */
    private Song getUserInput() {
        String title = txtfTitle.getText();
        String artist = txtfArtist.getText();
        String category = (String) cbCategory.getValue();
        String time = txtfTime.getText();
        String filePath = txtfFilePath.getText();

        int calculatedTime = newSongModel.calculateSecondsFromUserInput(time);

        // Check for invalid time input
        if (calculatedTime == -1) {
            showAlert("Invalid time input",  "Invalid time input: " + time + "\rtry format: 1.33.7");
            return null;
        }

        return new Song(title, artist, category, filePath, calculatedTime, -1);
    }

    /**
     *  A setter method for selectedSong
     *  populates the txt fields with the current information from the database.
     *
     * @param updatedSong
     */
    public void setUpdatedSong(Song updatedSong) {
        this.selecetedSong = updatedSong;
        if (updatedSong != null)
        {
            txtfTitle.setText(updatedSong.getTitle());
            txtfArtist.setText(updatedSong.getArtist());
            cbCategory.setValue(updatedSong.getCategory());
            txtfTime.setText(updatedSong.getFormattedTime());
            txtfFilePath.setText(updatedSong.getFilePath());
        }
    }

    /**
     * Code for updating the existing song with new details.
     */
    private void updateExistingSong(Song updatedSong){
        selecetedSong.setTitle(updatedSong.getTitle());
        selecetedSong.setArtist(updatedSong.getArtist());
        selecetedSong.setCategory(updatedSong.getCategory());
        selecetedSong.setFilePath(updatedSong.getFilePath());
        selecetedSong.setSeconds(updatedSong.getSeconds());
    }

    /**
     * Shows an alert dialog with the given title and content.
     *
     * @param title
     * @param content
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Method to create a popup for displaying errors
     *
     * @param title
     * @param content
     * @param e
     */
    private void displayError(String title, String content, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("There was a problem processing your request.");
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Code for closing the NewSong window
     */
    private void closeWindow(){
        Stage stage = (Stage) txtfTitle.getScene().getWindow();
        stage.close();
    }





}
