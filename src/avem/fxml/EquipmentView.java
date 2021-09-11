package avem.fxml;

import avem.basic.AVEMWindows;
import avem.core.AVEquipment;
import avem.basic.AVEMTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class EquipmentView {


    private static boolean verifiedAdd = false;
    private static GridPane mainGridPane;
    private static AVEquipment avEquipment;
    private static HBox hbox;
    private static Button btnMainFunction;
    private static Button btnCancel;
    private static Button btnClear;
    private static Button btnFill;
    private static VBox mainVbox;
    private static Scene scene;
    private static Stage window;
    private static Label lblHeader;
    private static Label lblContent;
    private static String[] labelsString;
    private static String[] fillString;
    private static ArrayList<Label> labels;
    private static ArrayList<TextField> textFields;
    private static int MAX_ROW_LABELS;

    private static TextArea txtDefectReport;
    private static String defectReport;


    public static AVEquipment displayAdd() {

        avEquipment = new AVEquipment("NONE", "NONE", "NONE");

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Equipment");
        window.initStyle(StageStyle.UNDECORATED);


        lblHeader = new Label();
        lblHeader.setText("Add New Audio-Visual Equipment");
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);


        labelsString = new String[]{
                "Name", "Type", "Serial Number", "Brand", "Current Location"};

        fillString = new String[]{
                "Lenovo 330", "Laptop", "SN9321", "Lenovo", "Sa imong heart."};

        labels = new ArrayList<>();
        textFields = new ArrayList<>();

        for (int i = 0; i < labelsString.length; i++) {
            labels.add(new Label(labelsString[i]));
            textFields.add(new TextField());
        }

        Label lblNotes = new Label("Equipment Notes");
        TextArea txtNotes = new TextArea();
        txtNotes.setMaxWidth(250);
        txtNotes.setWrapText(true);

        MAX_ROW_LABELS = labels.toArray().length;

        btnMainFunction = new Button("Add");
        btnCancel = new Button("Cancel");
        btnClear = new Button("Clear");
        btnFill = new Button("Fill");

        btnMainFunction.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Adding equipment", "Confirm adding of equipment?")) {
                if (notFilled()) {
                    AVAlert.showMessage("Notice", "All details are required to fill.");
                } else {
                    verifiedAdd = true;
                    window.close();
                }
            }
        });

        btnFill.setOnAction(actionEvent -> {
            for (int i = 0; i < MAX_ROW_LABELS; i++) {
                textFields.get(i).setText(fillString[i]);
            }

            txtNotes.appendText("None");
        });

        btnClear.setOnAction(actionEvent -> {
            for (int i = 0; i < MAX_ROW_LABELS; i++) {
                textFields.get(i).clear();
            }

            txtNotes.clear();
        });

        btnCancel.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Cancel", "Cancel adding of equipment?")) {
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
        hbox.getChildren().addAll(btnMainFunction, btnFill, btnClear, btnCancel);
        hbox.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, mainGridPane, lblNotes, txtNotes, hbox);
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
            System.out.println("VERIFIED ADD");
            avEquipment = new AVEquipment(textFields.get(0).getText(), textFields.get(1).getText(), textFields.get(2).getText());
            avEquipment.setBrand(textFields.get(3).getText());
            avEquipment.setNotes(txtNotes.getText());
            avEquipment.setCurrentLocation(textFields.get(4).getText());
            return avEquipment;
        }

        System.out.println("Add equipment failed.");
        return null;
    }

    public static AVEquipment displayEdit(AVEquipment toEdit) {

        String[] oldEquipmentDataString = new String[]{
                toEdit.getName(), toEdit.getType(), toEdit.getSerialNumber(), toEdit.getBrand(),
                toEdit.getCurrentLocation()};

        avEquipment = new AVEquipment("NONE", "NONE", "NONE");

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Equipment");
        window.initStyle(StageStyle.UNDECORATED);

        lblHeader = new Label();
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        lblHeader.setText("Edit Audio-Visual Equipment");
        lblHeader.setAlignment(Pos.BOTTOM_CENTER);


        lblContent = new Label();
        lblContent.setText("Editing Name, Type, and Serial No. \nare restricted to avoid ID conflicts.");
        lblContent.setTextAlignment(TextAlignment.CENTER);
        lblContent.setAlignment(Pos.CENTER);

        labelsString = new String[]{
                "Name", "Type", "Serial Number", "Brand", "Current Location"};


        labels = new ArrayList<>();
        textFields = new ArrayList<>();

        for (int i = 0; i < labelsString.length; i++) {
            labels.add(new Label(labelsString[i]));
            TextField field = new TextField(oldEquipmentDataString[i]);

            // disable editing of Name, Type, and Serial number to avoid discrepancies
            // of equipment ID number and reservation error in the long term

            if (i < 3) {
                field.setEditable(false);
            }

            textFields.add(field);
        }

        Label lblNotes = new Label("Equipment Notes");
        TextArea txtNotes = new TextArea();
        txtNotes.appendText(toEdit.getNotes());
        txtNotes.setMaxWidth(250);
        txtNotes.setMaxHeight(200);
        txtNotes.setWrapText(true);

        MAX_ROW_LABELS = labels.toArray().length;

        btnMainFunction = new Button("Edit");
        btnCancel = new Button("Cancel");


        btnMainFunction.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Confirm", "Confirm current action?")) {
                if (notFilled()) {
                    AVAlert.showMessage("Notice", "All details are required to fill.");
                } else {
                    verifiedAdd = true;
                    window.close();
                }
            }
        });

        btnCancel.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Cancel", "Cancel current action?")) {
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
        hbox.getChildren().addAll(btnMainFunction, btnCancel);
        hbox.setAlignment(Pos.CENTER);

        mainVbox = new VBox();
        mainVbox.getChildren().addAll(lblHeader, lblContent, mainGridPane, lblNotes, txtNotes, hbox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(20);
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
            System.out.println("VERIFIED EDIT");
            avEquipment = new AVEquipment(textFields.get(0).getText(), textFields.get(1).getText(), textFields.get(2).getText());
            avEquipment.setBrand(textFields.get(3).getText());
            avEquipment.setNotes(txtNotes.getText());
            avEquipment.setCurrentLocation(textFields.get(4).getText());
            System.out.println(avEquipment.getFormattedString());
            return avEquipment;
        }

        System.out.println("Edit equipment failed.");
        return null;
    }

    public static void view(AVEquipment toEdit) {

        String[] oldEquipmentDataString = new String[]{
                toEdit.getName(), toEdit.getType(), toEdit.getSerialNumber(), toEdit.getBrand(),
                toEdit.getCurrentLocation()};

        avEquipment = new AVEquipment("NONE", "NONE", "NONE");

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View Equipment");
        window.initStyle(StageStyle.UNDECORATED);

        lblHeader = new Label();
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        lblHeader.setText("View Equipment");

        labelsString = new String[]{
                "Name", "Type", "Serial Number", "Brand", "Storage Location"};


        labels = new ArrayList<>();
        textFields = new ArrayList<>();

        for (int i = 0; i < labelsString.length; i++) {
            labels.add(new Label(labelsString[i]));
            textFields.add(new TextField(oldEquipmentDataString[i]));
            textFields.get(i).setEditable(false);
        }

        Label lblNotes = new Label("Equipment Notes");
        TextArea txtNotes = new TextArea();
        txtNotes.appendText(toEdit.getNotes());
        txtNotes.setMaxWidth(250);
        txtNotes.setWrapText(true);
        txtNotes.setEditable(false);

        MAX_ROW_LABELS = labels.toArray().length;

        btnMainFunction = new Button("Close");

        btnMainFunction.setOnAction(actionEvent -> {
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
        hbox.getChildren().addAll(btnMainFunction);
        hbox.setAlignment(Pos.CENTER);


        VBox vbox2 = new VBox(20);
        vbox2.getChildren().addAll(mainGridPane, lblNotes, txtNotes);
        vbox2.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, vbox2, hbox);
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

//        if (verifiedAdd) {
//            System.out.println("VERIFIED EDIT");
//            avEquipment = new AVEquipment(textFields.get(0).getText(), textFields.get(1).getText(), textFields.get(2).getText());
//            avEquipment.setBrand(textFields.get(3).getText());
//            avEquipment.setNotes(txtNotes.getText());
//            avEquipment.setCurrentLocation(textFields.get(4).getText());
//            System.out.println(avEquipment.getFormattedString());
//            return avEquipment;
//        }

//        System.out.println("Edit equipment failed.");
////        return null;
    }

    public static String getDefectReport() {

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Report Equipment Defects");
        window.initStyle(StageStyle.UNDECORATED);

        lblHeader = new Label();
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        lblHeader.setText("Report Equipment Defects");

        defectReport = "";
        txtDefectReport = new TextArea();

        lblContent = new Label();
        lblContent.setText("Please specify the details of the defect/s below: ");

        btnMainFunction = new Button("Submit Report");
        btnCancel = new Button("Cancel");

        btnMainFunction.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Confirmation", "Submit report?")) {
                defectReport = txtDefectReport.getText();
                window.close();
            }
        });

        btnCancel.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Cancel", "Cancel report?")) {
                window.close();
            }
        });

        hbox = new HBox(10);
        hbox.getChildren().addAll(btnMainFunction, btnCancel);
        hbox.setAlignment(Pos.CENTER);

        mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, lblContent, txtDefectReport, hbox);
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

        if (defectReport.length() > 0) {
            return defectReport;
        }

        System.out.println("Edit equipment failed.");
        return null;
    }

    public static boolean notFilled() {
        System.out.println("Not filled running...");
        for (int i = 0; i < textFields.toArray().length; i++) {
            System.out.println(textFields.get(i).getText().trim().length() == 0);
            if (textFields.get(i).getText().trim().length() == 0) {
                return true;
            }
        }
        return false;
    }
}
