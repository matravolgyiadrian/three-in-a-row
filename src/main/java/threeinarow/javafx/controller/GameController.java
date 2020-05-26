package threeinarow.javafx.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import threeinarow.saves.GameSave;
import threeinarow.saves.GameSaveDAO;
import threeinarow.state.Direction;
import threeinarow.state.Piece;
import threeinarow.state.ThreeInARowState;
import util.guice.PersistenceModule;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private Label turnLabel;

    @FXML
    private GridPane gameGrid;

    private String playerRed, playerBlue;
    private ThreeInARowState gameState;
    private Image red, blue, empty, pmoves;
    private List<Direction> possibleMoves;
    private boolean isHighlighted = false;
    private int clickedRow, clickedColumn;

    public void setPlayersName(String playerRed, String playerBlue){
        this.playerBlue = playerBlue;
        this.playerRed = playerRed;
        setTurnLabel();
    }

    @FXML
    public void initialize() {
        initImages();
        this.gameState = new ThreeInARowState();
        displayGameState();
    }

    public void handleMovement(MouseEvent mouseEvent){
        if(!this.gameState.isEnd()){
            handleClicks(mouseEvent);
        } else{
            handleGameEnding();
        }
    }

    private void handleClicks(MouseEvent mouseEvent){
        if(isHighlighted) {
            handleClickIfIsHighlighted(mouseEvent);
        } else{
            highlightLegalMoves(mouseEvent);
        }
    }

    private void handleClickIfIsHighlighted(MouseEvent mouseEvent){
        Node source = (Node) mouseEvent.getSource();
        int row = GridPane.getRowIndex(source);
        int column = GridPane.getColumnIndex(source);
        for(Direction direction:possibleMoves){
            if(row == clickedRow+direction.getDx() && column == clickedColumn+direction.getDy()){
                move(direction);
                break;
            }
            eraseHighlight();
        }
    }

    private void move(Direction direction){
        gameState.moveTo(clickedRow, clickedColumn, direction);
        displayGameState();
        setTurnLabel();
        isHighlighted = false;
        if(this.gameState.isEnd()){
            handleGameEnding();
        }
    }

    private void highlightLegalMoves(MouseEvent mouseEvent){
        Node source = (Node) mouseEvent.getSource();
        clickedRow = GridPane.getRowIndex(source);
        clickedColumn = GridPane.getColumnIndex(source);
        log.debug("Cell ({}, {}) is pressed", clickedRow, clickedColumn);
        Piece piece = gameState.getBoard()[clickedRow][clickedColumn];
        if(!piece.equals(Piece.EMPTY) && piece.equals(gameState.getTurn())) {
            possibleMoves = gameState.whereToMove(clickedRow, clickedColumn);
            for (Direction direction : possibleMoves) {
                ImageView view = (ImageView) gameGrid.getChildren().get((clickedRow + direction.getDx()) * 4 + clickedColumn + direction.getDy());
                view.setImage(pmoves);
            }
            isHighlighted = true;
        }
    }

    private void eraseHighlight(){
        for (Direction direction : possibleMoves) {
            ImageView view = (ImageView) gameGrid.getChildren().get((clickedRow + direction.getDx()) * 4 + clickedColumn + direction.getDy());
            view.setImage(empty);
        }
        isHighlighted = false;
    }

    private void handleGameEnding(){
        try {
            log.info("The game is over, {} won.", gameState.getWinner().name());
            openEndWindow();
            closeThisWindow();
        } catch(IOException e){
            log.error("End window cannot load");
        }
    }

    private void closeThisWindow(){
        turnLabel.getScene().getWindow().hide();
    }

    private void openEndWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/endWindow.fxml"));
        Parent root = fxmlLoader.load();
        EndWindowController controller = fxmlLoader.getController();

        if(gameState.getWinner().name().equals("RED")){
            controller.setWinnerLabel(playerRed.toUpperCase() + " WON");
        } else {
            controller.setWinnerLabel(playerBlue.toUpperCase() + " WON");
        }
        Stage stage = new Stage();
        stage.setTitle("The Winner");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void setTurnLabel(){
        if(this.gameState.getTurn().name().equals("RED")){
            turnLabel.setText(this.playerRed.toUpperCase() + "'S TURN");
        } else {
            turnLabel.setText(this.playerBlue.toUpperCase() + "'S TURN");
        }
    }

    public void displayGameState() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i * 4 + j);
                if (view.getImage() != null) {
                    log.trace("Image({}, {}) = {}", i, j, view.getImage().getUrl());
                }
                if(gameState.getBoard()[i][j].name().equals("RED")) {
                    view.setImage(red);
                }else if(gameState.getBoard()[i][j].name().equals("BLUE")){
                    view.setImage(blue);
                } else {
                    view.setImage(empty);
                }
            }
        }
    }

    private GameSave createGameSave(){
        String state = createStringFromGameState();
        GameSave savedGame = GameSave.builder()
                .redPlayerName(playerRed)
                .bluPlayerName(playerBlue)
                .state(state)
                .build();

        return savedGame;
    }

    private String createStringFromGameState(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<5; i++){
            for(int j=0; j<4; j++){
                sb.append(this.gameState.getBoard()[i][j].getValue());
            }
        }
        return sb.toString();
    }

    private void initImages(){
        red = new Image(getClass().getResource("/images/red.png").toExternalForm());
        blue = new Image(getClass().getResource("/images/blue.png").toExternalForm());
        empty = new Image(getClass().getResource("/images/empty.png").toExternalForm());
        pmoves = new Image(getClass().getResource("/images/possibleMove.png").toExternalForm());
    }

    public void handleCloseRequest() {
        try {
            openCloseWindow();
        } catch(IOException e){
            log.error("Close window cannot load.");
        }
    }

    private void openCloseWindow() throws IOException {
        char[] answer = new char[1];
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/closeWindow.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Three In A Row");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnHidden(e -> {
            answer[0] = fxmlLoader.<CloseWindowController>getController().getAnswer();
        });
        log.info("Close window is loading...");
        stage.showAndWait();
        handleCloseAction(answer[0]);
    }

    private void handleCloseAction(char toCheck){
        if(toCheck == 'd'){
            Platform.exit();
        }
        if(toCheck == 's'){
            Injector injector = Guice.createInjector(new PersistenceModule("three-in-a-row"));
            GameSaveDAO gameSaveDAO = injector.getInstance(GameSaveDAO.class);
            gameSaveDAO.persist(createGameSave());

            log.info("Game state saved");
            Platform.exit();
        }
        // 'c' is intentionally left out
    }


    public void setGameState(int[][] state){
        this.gameState = new ThreeInARowState(state);
    }

}
