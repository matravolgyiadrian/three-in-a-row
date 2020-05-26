package threeinarow.javafx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class EndWindowController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private Label winnerLabel;

    @FXML
    private Button playAgainButton;

    @FXML
    private Button closeButton;

    private String redPlayer, bluePlayer;

    public void setWinnerLabel(String winner){
        this.winnerLabel.setText(winner);
    }

    @FXML
    void handleCloseButton(ActionEvent event) {
        log.debug("{} is pressed", ((Button) event.getSource()).getText());
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void handlePlayAgainButton(ActionEvent event) {
        try {
            log.debug("{} is pressed", ((Button) event.getSource()).getText());
            log.info("The game is restarted");
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/launch.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Three In A Row");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            playAgainButton.getScene().getWindow().hide();
        } catch(IOException e){
            log.error("Launch window cannot load.");
        }
    }

}
