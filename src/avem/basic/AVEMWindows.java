package avem.basic;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AVEMWindows {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public void change(ActionEvent actionEvent, String toFXML) throws IOException {

        Parent viewParent = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
        Scene scene = new Scene(viewParent);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene.getStylesheets().add("avem/css/base.css");
        scene.setFill(Color.TRANSPARENT);
        window.setScene(scene);
        window.setTitle("AVEM v.xx");
        window.show();
    }

    public static void makeMovable (Stage window, Parent root) {
        try {
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    window.setX(event.getScreenX() - xOffset);
                    window.setY(event.getScreenY() - yOffset);
                }
            });

        } catch (Exception e) {
            // null for mouse presses will always be there
        }
    }

    public static void makeMovableScene (Stage window, Scene scene) {
        try {
            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    window.setX(event.getScreenX() - xOffset);
                    window.setY(event.getScreenY() - yOffset);
                }
            });
        } catch (Exception e) {
            // null for mouse presses will always be there
       }
    }

    public static void maintainPosition(Stage window, Scene scene) {

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        Double offSetX = 0.0;
        Double offSetY = 0.0;
        double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * offSetX;
        double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * offSetY;
        window.setX(x);
        window.setY(y);
    }
}
