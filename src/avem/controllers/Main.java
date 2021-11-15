package avem.controllers;

import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage window) throws Exception{
        initDarkMode();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SigningPage.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);
        window.initStyle(StageStyle.TRANSPARENT);
        window.setScene(scene);
        window.setTitle("AVEM v.xx");
        window.show();

        AVEMWindows.makeMovable(window, root);

        window.setOnCloseRequest(event -> {
            event.consume();
            quit(window);
        });
    }

    private void initDarkMode() {
        // default is Light mode, so switching will switch to Dark mode
        AVEMTheme.setTheme(AVEMTheme.DARK_MODE_VALUE, AVEMTheme.MAROON_GOLD);
    }

    public void quit(Stage stage) {
        boolean ok = AVAlert.confirmAction("Exiting AVEM", "Are you sure you want to quit?");
        if (ok) {
            stage.close();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
