package threeinarow.javafx.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import threeinarow.saves.GameSaveDAO;
import util.guice.PersistenceModule;

import java.io.IOException;

@Slf4j
public class LaunchController {

    @FXML
    private Button newGameButton;

    @FXML
    private Button loadGameButton;

    @FXML
    public void initialize(){
        if(!haveSavedGame()){
            loadGameButton.setDisable(true);
        }
    }

    @FXML
    public void launchLoadGame(ActionEvent event) {
        displaySavedGamesWindow();
        closeWindow();
    }

    @FXML
    public void launchNewGame(ActionEvent event) throws IOException {
        displayPlayersNameWindow();
        closeWindow();
    }

    private void displayPlayersNameWindow() {
        try {
            log.info("Loading Players Name window...");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/playersName.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Player's name");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch(IOException e){
            log.error("Players Name window cannot load");
        }
    }

    private void displaySavedGamesWindow(){
        try {
            log.info("Loading Saved Games window...");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/savedGames.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Saved Games");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(IOException e){
            log.error("Saved games window cannot load.");
        }
    }

    private void closeWindow(){
        newGameButton.getScene().getWindow().hide();
    }

    private boolean haveSavedGame(){
        Injector injector = Guice.createInjector(new PersistenceModule("three-in-a-row"));
        GameSaveDAO gameSaveDAO = injector.getInstance(GameSaveDAO.class);
        return !gameSaveDAO.findAll().isEmpty();
    }

}
