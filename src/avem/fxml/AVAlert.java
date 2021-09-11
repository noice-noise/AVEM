package avem.fxml;

import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class AVAlert {

    private static double WIDTH = 300.0;
    private static double HEIGHT = 400.0;
    static boolean isYes;
    private static Stage window;
    private static Label lblHeader;
    private static Button btnYes;
    private static Button btnNo;
    private static HBox mainHBox;
    private static VBox mainVbox;
    private static Scene scene;
    private static Button btnOk;
    private static Label lblContent;

    public static boolean confirmAction(String title, String message) {
        window = new Stage();
        window.setTitle(title);

        lblHeader = new Label();
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        lblHeader.setText(title);
        lblHeader.setTextAlignment(TextAlignment.CENTER);

        lblContent = new Label(message);
        lblContent.setTextAlignment(TextAlignment.CENTER);

        btnYes = new Button("Yes");
        btnNo = new Button("No");

        btnYes.setOnAction(actionEvent -> {
            System.out.println("Pressed Yes.");
            isYes = true;
            window.close();
        });

        btnNo.setOnAction(actionEvent -> {
            System.out.println("Pressed No.");
            isYes = false;
            window.close();
        });

        mainHBox = new HBox(50);
        mainHBox.getChildren().addAll(btnYes, btnNo);
        mainHBox.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, lblContent, mainHBox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));
        runBoxrunnnnn();

        return isYes;
    }

    public static void showMessage(String title, String message) {
        window = new Stage();
        window.setTitle(title);
//        HEIGHT = 300.0;
//        WIDTH = 350.0;

        lblHeader = new Label(title);
        lblHeader.setText(title);
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);

        lblContent = new Label(message);
        lblContent.setTextAlignment(TextAlignment.CENTER);

        btnOk = new Button("Ok");


        btnOk.setOnAction(actionEvent -> {
            window.close();
        });

        mainHBox = new HBox(10);
        mainHBox.getChildren().addAll(btnOk);
        mainHBox.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, lblContent, mainHBox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));

        runBoxrunnnnn();
    }

    public static void showMessage(String title, String message, double height, double width) {
        WIDTH = width;
        HEIGHT = height;

        window = new Stage();
        window.setHeight(HEIGHT);
        window.setWidth(WIDTH);

        lblHeader = new Label(title);
        lblHeader.setText(title);
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);

        lblContent = new Label(message);
        lblContent.setTextAlignment(TextAlignment.CENTER);

        btnOk = new Button("Ok");


        btnOk.setOnAction(actionEvent -> {
            window.close();
        });

        mainHBox = new HBox(10);
        mainHBox.getChildren().addAll(btnOk);
        mainHBox.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, lblContent, mainHBox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));

        runBoxrunnnnn();
    }

    public static void runBoxrunnnnn() {
        scene = new Scene(mainVbox);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.TRANSPARENT);
        window.resizableProperty().setValue(false);
        window.setScene(scene);

        AVEMWindows.makeMovableScene(window, scene);
        window.showAndWait();
    }
}
