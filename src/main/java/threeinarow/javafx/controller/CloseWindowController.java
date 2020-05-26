package threeinarow.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloseWindowController {

    private static char answer;

    @FXML
    private Button saveButton;

    @FXML
    private Button dontSaveButton;

    @FXML
    private Button cancelButton;

    @FXML
    void clickedCancelButton(ActionEvent event) {
        log.debug("{} is pressed", ((Button) event.getSource()).getText());
        answer = 'c';
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    void clickedDontSaveButton(ActionEvent event) {
        log.debug("{} is pressed", ((Button) event.getSource()).getText());
        answer = 'd';
        dontSaveButton.getScene().getWindow().hide();
    }

    @FXML
    void clickedSaveButton(ActionEvent event) {
        log.debug("{} is pressed", ((Button) event.getSource()).getText());
        answer = 's';
        saveButton.getScene().getWindow().hide();
    }

    public char getAnswer(){
        return answer;
    }



}
