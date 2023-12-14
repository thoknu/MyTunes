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
    public void setMainController(MainController mainController) {
    }

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
        Stage stage = (Stage) txtfTitle.getScene().getWindow();
        stage.close();
    }

    private void handleNewSong() {

        // gets all user input.
        String title = txtfTitle.getText();
        String artist = txtfArtist.getText();
        String category = (String) cbCategory.getValue();
        String time = txtfTime.getText();
        String filePath = txtfFilePath.getText();

        // calculates the time to seconds.
        int calculatedTime = newSongModel.calculateSecondsFromUserInput(time);
        // invalid input
        if (calculatedTime == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid time input: " + time + "\rtry format: 1.33.7");
            alert.showAndWait();
            return;
        }

        Song newSong = new Song(title, artist, category, filePath, calculatedTime, -1);

        try {
            newSongModel.createNewSong(newSong);
            showConfirmation("Song Added", " has been successfully added.");
        } catch (Exception e) {
            displayError("Error", "An error occurred while adding the song.", e);
        }
        mainModel.refreshSongs();
    }
    private void handleSongEdit() {
        if (selecetedSong != null) {
            // gets all user input and sets it.
            String title = txtfTitle.getText();
            String artist = txtfArtist.getText();
            String category = (String) cbCategory.getValue();
            String time = txtfTime.getText();
            String filePath = txtfFilePath.getText();

            int calculatedTime = newSongModel.calculateSecondsFromUserInput(time);
            // invalid time input.
            if (calculatedTime == -1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid time input: " + time + "\rtry format: 1.33.7");
                alert.showAndWait();
                return;
            }

            // updates the existing song
            selecetedSong.setTitle(title);
            selecetedSong.setArtist(artist);
            selecetedSong.setCategory(category);
            selecetedSong.setFilePath(filePath);
            selecetedSong.setSeconds(calculatedTime);

            try {
                newSongModel.updateSong(selecetedSong);
                showConfirmation("Song Updated", "The song has been successfully updated.");
                mainModel.refreshSongs();
            } catch (Exception e) {
                displayError("Error", "An error occurred while updating the song.", e);
            }
        }
    }

    public void onCancelSong(ActionEvent actionEvent) {
        Stage stage = (Stage) txtfTitle.getScene().getWindow();
        stage.close();
    }


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

    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

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
