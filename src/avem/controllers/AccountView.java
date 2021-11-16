package avem.controllers;

import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import avem.core.AVAccount;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Locale;

public class AccountView {
    private static boolean verifiedAdd = false;
    private static GridPane mainGridPane;
    private static AVAccount avAccount;
    private static HBox hbox;
    private static Button btnMainFunction;
    private static Button btnCancel;
    private static Button btnClear;
    private static VBox mainVbox;
    private static Scene scene;
    private static Stage window;
    private static Label lblHeader;
    private static String[] labelsString;
    private static String[] fillString;
    private static ArrayList<Label> labels;
    private static ArrayList<TextField> textFields;
    private static int MAX_ROW_LABELS;

    private static int VALID_PASSWORD_LENGTH = 4;

    public static AVAccount displayAdd() {

        avAccount = new AVAccount("NONE", "NONE", AVAccount.STUDENT_GUEST, "None", "None");

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Sign up Account");
        window.initStyle(StageStyle.UNDECORATED);

        lblHeader = new Label();
        lblHeader.setText("Sign up New Account");
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);

        labelsString = getStringLabels();

        labels = new ArrayList<>();
        textFields = new ArrayList<>();

        for (int i = 0; i < labelsString.length; i++) {
            labels.add(new Label(labelsString[i]));
            textFields.add(new TextField());
        }

        MAX_ROW_LABELS = labels.toArray().length;

        btnMainFunction = new Button("Sign up");
        btnCancel = new Button("Cancel");
        btnClear = new Button("Clear");

        btnMainFunction.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Sign up account", "Confirm signing up of new account?")) {

                if (!allInputsFilled()) {
                    AVAlert.showMessage("Notice", "All details are required to fill.");
                } else if (!matchedPasswords()) {
                    AVAlert.showMessage("Notice", "Passwords don't match.");
                } else if (!validPasswordLength()) {
                    AVAlert.showMessage("Invalid Password Length.", "Password length must be more than 4 characters.");
                } else if (!verifiedAccountAccess()) {
                    AVAlert.showMessage("Account Access Type Invalid.", "Please enter valid account access: \nAdmin, Staff, or Guest.");
                } else {
                    verifiedAdd = true;
                    window.close();
                }
            }
        });

        btnClear.setOnAction(actionEvent -> {
            for (int i = 0; i < MAX_ROW_LABELS; i++) {
                textFields.get(i).clear();
            }
        });

        btnCancel.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Cancel", "Cancel signing up of account?")) {
                window.close();
            }
        });

        mainGridPane = new GridPane();

        for (int i = 0; i < labels.toArray().length; i++) {
            mainGridPane.add(labels.get(i), 1, i);
            mainGridPane.add(textFields.get(i), 2, i);
        }

        mainGridPane.setAlignment(Pos.CENTER);
        mainGridPane.setVgap(10);
        mainGridPane.setHgap(20);

        hbox = new HBox(10);
        hbox.getChildren().addAll(btnMainFunction, btnClear, btnCancel);
        hbox.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, mainGridPane, hbox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));

        scene = new Scene(mainVbox);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);

        window.initStyle(StageStyle.TRANSPARENT);
        window.resizableProperty().setValue(false);
        window.setScene(scene);
        AVEMWindows.makeMovableScene(window, scene);
        window.showAndWait();


        if (verifiedAdd) {
            textFields.get(0).getText();

            avAccount = new AVAccount(
                    textFields.get(0).getText(),    //username
                    textFields.get(1).getText(),  //password
                    parseAccountAccessValue(textFields.get(3).getText()),    //access
                    textFields.get(4).getText(),    //name
                    textFields.get(5).getText()   //additional info
            );

            return avAccount;
        }

        return null;
    }

    private static String[] getStringLabels() {
        // the commented code statement below is the original functional code
//        labelsString = new String[]{
//                "Username", "Password", "Confirm Password", "Account Type", "Name", "Additional Info"};

        // the code statement below IS NOT PROPERLY IMPLEMENTED and
        // is a modified form for demo purposes
        String[] labelsString = new String[]{
                "Username", "Password", "Confirm Password", "Account Type", "Email", "Department/Course", "Name", "Additional Info"};
        return labelsString;
    }

    private static int parseAccountAccessValue(String toParse) {
        toParse = toParse.toUpperCase(Locale.ROOT);
        if (toParse.equals("ADMIN")) {
            return AVAccount.ADMIN;
        } else if (toParse.equals("STAFF")) {
            return AVAccount.INTERNAL_STAFF;
        } else if (toParse.equals("GUEST")) {
            return AVAccount.STUDENT_GUEST;
        }

        return AVAccount.STUDENT_GUEST;
    }

    private static String parseAccountAccessString(int toParse) {

        if (toParse == AVAccount.ADMIN) {
            return "ADMIN";
        } else if (toParse == AVAccount.INTERNAL_STAFF) {
            return "STAFF";
        } else if (toParse == AVAccount.STUDENT_GUEST) {
            return "GUEST";
        }

        throw new IllegalArgumentException("Account access invalid.");
    }

    private static boolean verifiedAccountAccess() {
        int accountAccessIndex = 3;
        String accessStr = textFields.get(accountAccessIndex).getText().toUpperCase(Locale.ROOT);

        if (accessStr.equals("ADMIN") || accessStr.equals("STAFF") || accessStr.equals("GUEST")) {
            return true;
        }

        return false;
    }

    private static boolean matchedPasswords() {
        int passwordIndex = 1;
        int confirmPasswordIndex = 2;


        if (textFields.get(passwordIndex).getText().trim().equals(textFields.get(confirmPasswordIndex).getText().trim())) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean validPasswordLength() {
        int passwordIndex = 1;

        if (textFields.get(passwordIndex).getText().trim().length() > VALID_PASSWORD_LENGTH) {
            return true;
        } else {
            return false;
        }
    }

    public static AVAccount displayEdit(AVAccount toEdit) {

        String[] oldAccountDataString = new String[]{
                toEdit.getUsername(), toEdit.getPassword(), "", parseAccountAccessString(toEdit.getAccess()),
        toEdit.getName(), toEdit.getAdditionalInfo()};

        avAccount = new AVAccount("NONE", "NONE", AVAccount.STUDENT_GUEST, "None", "None");

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Account");
        window.initStyle(StageStyle.UNDECORATED);

        lblHeader = new Label();
        lblHeader.setText("Edit Account");
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);

        labelsString = new String[]{
                "Username", "Password", "Confirm Password", "Account Type", "Name", "Additional Info"};

        labels = new ArrayList<>();
        textFields = new ArrayList<>();

        for (int i = 0; i < labelsString.length; i++) {
            labels.add(new Label(labelsString[i]));
            textFields.add(new TextField(oldAccountDataString[i]));
        }

        MAX_ROW_LABELS = labels.toArray().length;

        btnMainFunction = new Button("Confirm Edit");
        btnCancel = new Button("Cancel");
        btnClear = new Button("Clear");

        btnMainFunction.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Editing of account", "Confirm editing of account?")) {

                if (!allInputsFilled()) {
                    AVAlert.showMessage("Notice", "All details are required to fill.");
                } else if (!matchedPasswords()) {
                    AVAlert.showMessage("Notice", "Passwords don't match.");
                } else if (!validPasswordLength()) {
                    AVAlert.showMessage("Invalid Password Length.", "Password length must be more than 4 characters.");
                } else if (!verifiedAccountAccess()) {
                    AVAlert.showMessage("Account Access Type Invalid.", "Please enter valid account access: \nAdmin, Staff, or Guest.");
                } else {
                    verifiedAdd = true;
                    window.close();
                }
            }
        });


        btnClear.setOnAction(actionEvent -> {
            for (int i = 0; i < MAX_ROW_LABELS; i++) {
                textFields.get(i).clear();
            }
        });

        btnCancel.setOnAction(actionEvent -> {
            window.close();
        });

        mainGridPane = new GridPane();

        for (int i = 0; i < labels.toArray().length; i++) {
            mainGridPane.add(labels.get(i), 1, i);
            mainGridPane.add(textFields.get(i), 2, i);
        }

        mainGridPane.setAlignment(Pos.CENTER);
        mainGridPane.setVgap(10);
        mainGridPane.setHgap(20);

        hbox = new HBox(10);
        hbox.getChildren().addAll(btnMainFunction, btnClear, btnCancel);
        hbox.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, mainGridPane, hbox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));

        scene = new Scene(mainVbox);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);

        window.initStyle(StageStyle.TRANSPARENT);
        window.resizableProperty().setValue(false);
        window.setScene(scene);
        AVEMWindows.makeMovableScene(window, scene);
        window.showAndWait();


        if (verifiedAdd) {
            textFields.get(0).getText();

            avAccount = new AVAccount(
                    textFields.get(0).getText(),    //username
                    textFields.get(1).getText(),  //password
                    parseAccountAccessValue(textFields.get(3).getText()),    //access
                    textFields.get(4).getText(),    //name
                    textFields.get(5).getText()   //additional info
            );

            return avAccount;
        }

        System.out.println("Edit equipment failed.");
        return null;
    }

    public static boolean allInputsFilled() {
        for (int i = 0; i < textFields.toArray().length; i++) {
            System.out.println(textFields.get(i).getText().trim().length() == 0);
            if (textFields.get(i).getText().trim().length() == 0) {
                return false;
            }
        }
        return true;
    }
}
