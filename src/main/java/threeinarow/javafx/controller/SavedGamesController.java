package threeinarow.javafx.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import threeinarow.saves.GameSave;
import threeinarow.saves.GameSaveDAO;
import util.guice.PersistenceModule;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Slf4j
public class SavedGamesController {

    @FXML
    private TableView<GameSave> savedGamesTable;

    @FXML
    private TableColumn<GameSave, String> redPlayer;

    @FXML
    private TableColumn<GameSave, String> bluePlayer;

    @FXML
    private TableColumn<GameSave, ZonedDateTime> created;
    @FXML
    private Button loadButton;


    @FXML
    private void initialize(){
        log.debug("Loading saved games...");
        displayList();
    }

    private void displayList(){
        Injector injector = Guice.createInjector(new PersistenceModule("three-in-a-row"));
        GameSaveDAO gameSaveDAO = injector.getInstance(GameSaveDAO.class);
        List<GameSave> savedGames = gameSaveDAO.findAll();

        redPlayer.setCellValueFactory(new PropertyValueFactory<>("redPlayerName"));
        bluePlayer.setCellValueFactory(new PropertyValueFactory<>("bluPlayerName"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));

        created.setCellFactory(column -> {
            TableCell<GameSave, ZonedDateTime> cell = new TableCell<GameSave, ZonedDateTime>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    } else {
                        setText(item.format(formatter));
                    }
                }
            };
            return cell;
        });

        ObservableList<GameSave> observableList = FXCollections.observableArrayList();
        observableList.addAll(savedGames);

        savedGamesTable.setItems(observableList);
    }

    @FXML
    void deleteSelectedGame() {
        ObservableList<GameSave> selectedGame;
        selectedGame = savedGamesTable.getSelectionModel().getSelectedItems();
        log.debug("Selected game is {}", selectedGame.get(0));
        Injector injector = Guice.createInjector(new PersistenceModule("three-in-a-row"));
        GameSaveDAO gameSaveDAO = injector.getInstance(GameSaveDAO.class);
        gameSaveDAO.deleteByID(selectedGame.get(0).getId());
        displayList();
    }

    @FXML
    public void loadSelectedGame(){
        ObservableList<GameSave> selectedGame;
        selectedGame = savedGamesTable.getSelectionModel().getSelectedItems();
        log.debug("Selected game is {}", selectedGame.get(0));
        openGameWindow(selectedGame.get(0));
    }

    private void openGameWindow(GameSave game){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            GameController gameController = fxmlLoader.getController();
            gameController.setPlayersName(game.getRedPlayerName(), game.getBluPlayerName());
            gameController.setGameState(makeValidBoard(game.getState()));
            gameController.displayGameState();
            Stage stage = new Stage();
            stage.setTitle("ThreeInARow");
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
            log.error("Game window cannot load");
        }
    }

    private int[][] makeValidBoard(String state){
        int[][] board = new int[5][4];
        for(int i=0;i<5;i++){
            for(int j=0; j<4; j++){
                board[i][j] = Character.getNumericValue(state.charAt(i*4+j));
            }
        }
        return board;
    }

    private void closeWindow(){
        loadButton.getScene().getWindow().hide();
    }
}
