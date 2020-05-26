package threeinarow.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class PlayersNameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField redPlayerName;

    @FXML
    private TextField bluePlayerName;

    @FXML
    private Button continueButton;

    @FXML
    private Label errorLabel;

    @FXML
    void handleContinueButton()  {
        if(redPlayerName.getText().isEmpty() || bluePlayerName.getText().isEmpty()){
            errorLabel.setText("Enter your names!!");
        } else {
            openGameScene();
        }
    }

    @FXML
    void handleEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            handleContinueButton();
        }
    }

    private void openGameScene(){
        try{
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            GameController gameController = fxmlLoader.getController();
            gameController.setPlayersName(redPlayerName.getText(), bluePlayerName.getText());
            log.info("The players name is set to {} and {}", redPlayerName.getText(), bluePlayerName.getText());
            Stage stage = new Stage();
            stage.setTitle("Three In A Row");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnCloseRequest(e -> {
                e.consume();
                gameController.handleCloseRequest();
            });
            log.info("Loading game window...");
            stage.show();
            closeWindow();
        } catch(IOException e){
            log.error("Game window cannot load.");
        }
    }

    private void closeWindow(){
        continueButton.getScene().getWindow().hide();
    }

}
