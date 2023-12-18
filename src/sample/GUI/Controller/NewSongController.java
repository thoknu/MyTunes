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

    @FXML
    public void initialize() throws Exception {
        newSongModel = new NewSongModel();
        mainModel = new MainModel();

        // sets the dropdown menu for all categories.
        cbCategory.getItems().clear();
        cbCategory.getItems().addAll("Blues","Classical","Country","Electronic","Funk","Instrumental","Jazz","Latin","Metal","Pop","Punk","R&B","Rap","Reggae","Rock","Techno","Miscellaneous");
        cbCategory.getSelectionModel().select("Pop");

    }

    // Ensures its the correct MainController used, so it isnt null.
    public void setMainController(MainController mainController) {
    }

    // Ensures its the correct mainModel, so it isnt null when called.
    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
    Method handling when the save button is pressed.
    * it either creates a new song and sends it to the database.
    or it handles if its a song edit, and updates it.
    */

    public void onSaveSong(ActionEvent actionEvent) {

        try {
            if (selecetedSong == null) {
                handleNewSong();
            } else {
                handleSongEdit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Gets the MainWindow again.
        Stage stage = (Stage) txtfTitle.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to create a new song down through the layers
     * @param newSong with the param from getUserInput
     * @ mainModel.refreshSongs(); updates the tableview for songs.
     */
    private void handleNewSong() {
        Song newSong = getUserInput();

        if (newSong != null) {
            try {
                newSongModel.createNewSong(newSong);
                showConfirmation("Song Added", " has been successfully added.");
            } catch (Exception e) {
                displayError("Error", "An error occurred while adding the song.", e);
            }
            mainModel.refreshSongs();
        }
    }

    /**
     * Method to handle a song edit
     * @param updatedSong with the param from getUserInput
     * selectedSong sets updated song with new params
     * newSongModel.updateSong(selecetedSong); sends it down to DAL
     */
    private void handleSongEdit() {
        Song updatedSong = getUserInput();

        if (updatedSong != null && selecetedSong != null) {
            // Update the existing song
            selecetedSong.setTitle(updatedSong.getTitle());
            selecetedSong.setArtist(updatedSong.getArtist());
            selecetedSong.setCategory(updatedSong.getCategory());
            selecetedSong.setFilePath(updatedSong.getFilePath());
            selecetedSong.setSeconds(updatedSong.getSeconds());

            try {
                newSongModel.updateSong(selecetedSong);
                showConfirmation("Song Updated", "The song has been successfully updated.");
                mainModel.refreshSongs();
            } catch (Exception e) {
                displayError("Error", "An error occurred while updating the song.", e);
            }
        }
    }

    /**
     * Method to handle user input
     * used to get the content of the different txtfields in the GUI
     * also calculates the time input, and checks if its valid.
     * @return
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid time input: " + time + "\rtry format: 1.33.7");
            alert.showAndWait();
            return null;
        }

        return new Song(title, artist, category, filePath, calculatedTime, -1);
    }

    /**
     * Method closes the FXML Window.
     * and gets the Main FXML Window agian.
     * @param actionEvent
     */
    public void onCancelSong(ActionEvent actionEvent) {
        Stage stage = (Stage) txtfTitle.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to create a popup message for confirmations etc.
     * @param title
     * @param content
     */
    private void showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Add a custom OK button to the dialog
        ButtonType okButton = new ButtonType("OK", ButtonType.OK.getButtonData());
        alert.getButtonTypes().setAll(okButton);

        // Set the action for the OK button
        alert.setOnHidden(event -> {
            if (alert.getResult() == okButton){}
        });

        alert.showAndWait();
    }

    /**
     * Method to create a popup for displaying errors
     * @param title
     * @param content
     * @param e
     */
    private void displayError(String title, String content, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();

        ButtonType okButton = new ButtonType("OK", ButtonType.OK.getButtonData());
        alert.getButtonTypes().setAll(okButton);

        alert.setOnHidden(event -> {
            if (alert.getResult() == okButton){}
        });

        e.printStackTrace();

        alert.showAndWait();
    }

    /**
     *  A setter method for selectedSong
     *  populates the txt fields with the current information from the database.
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
}
