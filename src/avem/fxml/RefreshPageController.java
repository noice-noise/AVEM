package avem.fxml;


import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

import static avem.basic.AVEMTheme.VERSION;

public class RefreshPageController {
    @FXML
    private Button btnContinue;

    public void switchToDashboard(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        window.setScene(scene);
        window.setTitle(VERSION);
        AVEMWindows.makeMovable(window, root);
        window.show();
        window.centerOnScreen();
    }
}
