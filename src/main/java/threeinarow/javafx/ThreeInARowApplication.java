package threeinarow.javafx;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import threeinarow.saves.GameSaveDAO;
import util.guice.PersistenceModule;

import java.util.List;

@Slf4j
public class ThreeInARowApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting application...");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        primaryStage.setTitle("Three In A Row");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
