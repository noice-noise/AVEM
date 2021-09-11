package avem.fxml;

import avem.basic.AVEMInfo;
import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import avem.core.AVEquipment;
import avem.core.AVEquipmentManager;
import avem.core.AVReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import static avem.basic.AVEMInfo.CURRENT_ACCOUNT;

public class CheckInOutEquipment implements Initializable {

    @FXML
    public static TableView<AVEquipment> tblEquipmentView;
    @FXML
    private static TableColumn<AVEquipment, String> equipmentId;
    @FXML
    private static TableColumn<AVEquipment, String> nameColumn;
    @FXML
    private static TableColumn<AVEquipment, String> typeColumn;
    @FXML
    private static TableColumn<AVEquipment, String> brandColumn;
    @FXML
    private static TableColumn<AVEquipment, String> serialNumberColumn;
    @FXML
    private static TableColumn<AVEquipment, String> locationColumn;
    //    @FXML
//    private static TableColumn<AVEquipment, Boolean> isAvailableColumn;
    @FXML
    private static TableColumn<AVEquipment, String> notesColumn;

    private static AVEquipmentManager equipmentManager;
    private static HashMap<String, AVEquipment> equipmentBundle;
    private static Stage window;
    private static Scene scene;
    private static String TITLE = "Available Equipment View";
    private static VBox mainVbox;
    private static HBox mainHBox;
    private static Button btnConfirm;
    private static Button btnMarkAll;
    private static Button btnReportDefects;
    private static Button btnClose;
    private static Label lblHeader;
    private static ObservableList<AVEquipment> selectedEquipment;
    private static Label lblContext;

    public static AVReservation getReservation() {
        return reservation;
    }

    private static AVReservation reservation;


    public static ObservableList<AVEquipment> viewEquipmentBundle(AVReservation r) {
        reservation = r;
        equipmentBundle = r.getEquipmentBundle();
        equipmentManager = new AVEquipmentManager();
        initializeView();

        // NOTE THIS IS THE LAST EXIT POINT OF THE CLASS
        // Returns either 2 cases:
        // 1. If User presses btnConfirm then return selectedEquipment
        // 2. If User presses cancel, then return null

        return selectedEquipment;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeView();
    }

    public static void initializeView() {
        initEquipmentTableView();
        initEquipmentColumns();
        updateEquipmentList();
        buildView();
        startTheParty();
    }


    private static void buildView() {

        lblHeader = new Label();
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        lblHeader.setText("Checking of Equipment");
        lblHeader.setTextAlignment(TextAlignment.CENTER);
        lblHeader.setAlignment(Pos.TOP_CENTER);
        lblHeader.setPadding(new Insets(0, 0, 0, 0));

        lblContext = new Label();
        lblContext.setText("Press \"Confirm\" if no defects were found or \"Report Defect/s\" to submit a detailed report.");
        lblContext.setTextAlignment(TextAlignment.CENTER);
        lblContext.setAlignment(Pos.CENTER);
        lblContext.setPadding(new Insets(0, 0, 0, 0));

        mainVbox = new VBox(20);
        mainVbox.setAlignment(Pos.CENTER);

        btnConfirm = new Button("Confirm");
        btnMarkAll = new Button("Check out ALL");
        btnReportDefects = new Button("Report Defect/s");
        btnClose = new Button("Close");

        btnConfirm.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Confirm", "Confirm checking?")) {

                selectedEquipment = tblEquipmentView.getItems();

                if (selectedEquipment == null || selectedEquipment.size() == 0) {
                    AVAlert.showMessage("No equipment", "No equipment selected.");
                }

                window.close();
            }
        });

        btnMarkAll.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Reserve Equipment", "Reserve all selected equipment?")) {

                selectedEquipment = tblEquipmentView.getItems();
                window.close();
            }
        });


        btnReportDefects.setOnAction(actionEvent -> {

            selectedEquipment = tblEquipmentView.getSelectionModel().getSelectedItems();
            if (selectedEquipment.size() != 1) {
                AVAlert.showMessage("Invalid", "Please select only one specific equipment.");
            } else if (selectedEquipment.size() == 1) {
                try {

                    AVEquipment selectedAVEquipment = selectedEquipment.get(0);

                    if (selectedAVEquipment == null) {
                        AVAlert.showMessage("Select Equipment", "Please select an equipment to edit.");
                    } else {
                        AVEquipment newAVE = selectedAVEquipment;
                        String defectReport = getDefectReport();

                        if (newAVE != null && defectReport != null && defectReport.trim().length() != 0) {
                            newAVE.appendNotes(defectReport);   // append on newAVE

                            equipmentManager.editEquipment(selectedAVEquipment, newAVE);    // append on equipment manager copy
                            tblEquipmentView.refresh();
                            AVAlert.showMessage("Successful", "Defect Report has been submitted.\nEquipment list has been updated successfully.");
                            AVEMInfo.updateRecentActivity(CURRENT_ACCOUNT + " reported a defect on: " + selectedAVEquipment.getName() + " equipment.");
                        }
                    }
                } catch (NullPointerException ne) {
                    // null exceptions from defectReport means that the operation has been cancelled.
                } catch (IllegalArgumentException ia) {
                    AVAlert.showMessage("Notice", ia.getMessage());
                } catch (Exception e) {
                    AVAlert.showMessage("Error", e.toString());
                    e.printStackTrace();
                }
            }
        });

        btnClose.setOnAction(actionEvent -> {
            selectedEquipment = null;
            window.close();
        });

        if (selectedEquipment != null) {
            selectAllPreSelectedShit();
        }

        mainHBox = new HBox(50);
        mainHBox.getChildren().addAll(btnConfirm, btnReportDefects, btnClose);
        mainHBox.setAlignment(Pos.TOP_CENTER);
        mainVbox.setPadding(new Insets(0, 50, 0, 50));
        mainVbox.getChildren().addAll(lblHeader, lblContext, tblEquipmentView, mainHBox);
    }

    private static String getDefectReport() {
        String report = "";
        try {
            report = EquipmentView.getDefectReport();

            if (report == null) {
                throw new NullPointerException();
            }

            if (report.trim().length() < 0) {
                throw new IllegalArgumentException("Sorry, report is empty.");
            }


        } catch (Exception e) {

        }

        return report;
    }

    private static void initEquipmentTableView() {
        tblEquipmentView = new TableView<>();
        tblEquipmentView.setMaxHeight(300);

        equipmentId = new TableColumn<AVEquipment, String>("ID");
        nameColumn = new TableColumn<AVEquipment, String>("NAME");
        typeColumn = new TableColumn<AVEquipment, String>("TYPE");
        brandColumn = new TableColumn<AVEquipment, String>("BRAND");
        serialNumberColumn = new TableColumn<>("SERIAL ID");
        locationColumn = new TableColumn<AVEquipment, String>("CURRENT LOCATION");
        notesColumn = new TableColumn<AVEquipment, String>("NOTES");
    }

    private static void selectAllPreSelectedShit() {
        System.out.println("selectAllPreSelectedShit invoked... \nselectedEquipment size: " + selectedEquipment.size());
        ObservableList<AVEquipment> items = tblEquipmentView.getItems();
        HashSet<Integer> keys = new HashSet<>();

        for (int i = 0; i < selectedEquipment.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                System.out.println("COMPARING..: " + selectedEquipment.get(i).getName() + ":" + items.get(j).getName());
                if (selectedEquipment.get(i).equals(items.get(j))) {
                    System.out.println("MATCH: " + selectedEquipment.get(i).getName() + ":" + items.get(j).getName());
                    System.out.println("Selecting..." + items.get(j).getName());
                    System.out.println("Index: " + j);
                    tblEquipmentView.getSelectionModel().clearSelection();
                    keys.add(j);
                    tblEquipmentView.getSelectionModel().select(items.get(j));
                }
            }
        }

        keys.forEach(index -> tblEquipmentView.getSelectionModel().select(index));
    }

    private static void startTheParty() {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(TITLE);
        window.setWidth(770);
        window.setHeight(600);

        scene = new Scene(mainVbox);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);

        window.initStyle(StageStyle.TRANSPARENT);
        window.resizableProperty().setValue(false);
        window.setScene(scene);
        AVEMWindows.makeMovable(window, scene.getRoot());
        window.showAndWait();
        window.centerOnScreen();
    }

    public static void setPreSelectedEquipment(ObservableList<AVEquipment> preSelected, ObservableList<AVEquipment> preSelectedEquipment) {
        selectedEquipment = preSelectedEquipment;
    }

    private static ObservableList<AVEquipment> getEquipmentList() {
        ObservableList<AVEquipment> equipmentObservableList = FXCollections.observableArrayList();

        for (AVEquipment ave :
                equipmentBundle.values()) {
            equipmentObservableList.add(ave);
        }

        return equipmentObservableList;
    }

    public static void initEquipmentColumns() {
        equipmentId.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("equipmentID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("type"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("brand"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("serialNumber"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("currentLocation"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("notes"));

//        nameColumn.setCellFactory(tc -> {
//            TableCell<AVEquipment, String> cell = new TableCell<>();
//            Text text = new Text();
//            cell.setGraphic(text);
//            cell.prefHeight(Control.USE_COMPUTED_SIZE);
//            text.wrappingWidthProperty().bind(nameColumn.widthProperty());
//            text.textProperty().bind(cell.itemProperty());
//            text.setTextAlignment(TextAlignment.CENTER);
//            cell.setPadding(new Insets(10, 10, 10, 10));
//            return cell;
//        });

//        isAvailableColumn.setCellFactory(col -> new TableCell<AVEquipment, Boolean>() {
//            @Override
//            protected void updateItem(Boolean item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty ? null : item ? "Available" : "Reserved");
//            }
//        });

        tblEquipmentView.getColumns().removeAll();
        tblEquipmentView.getColumns().clear();
        tblEquipmentView.getColumns().addAll(equipmentId,
                nameColumn, typeColumn, brandColumn,
                serialNumberColumn, locationColumn, notesColumn);
    }

    private static void updateEquipmentList() {
        tblEquipmentView.setItems(getEquipmentList());
        tblEquipmentView.getSortOrder().add(typeColumn);
    }
}
