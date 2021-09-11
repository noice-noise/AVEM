package avem.fxml;

import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import avem.core.AVEquipment;
import avem.core.AVEquipmentManager;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ReserveEquipment implements Initializable {

    @FXML
    public static MultiSelectTableView<AVEquipment> tblEquipmentView;
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
    @FXML
    private static TableColumn<AVEquipment, Boolean> isAvailableColumn;
    @FXML
    private static TableColumn<AVEquipment, String> notesColumn;


    private static AVEquipmentManager equipmentManager;
    private static Stage window;
    private static Scene scene;
    private static String TITLE = "Available Equipment View";
    private static VBox mainVbox;
    private static HBox mainHBox;
    private static Button btnConfirm;
    private static Button btnCancel;
    private static Label lblHeader;
    private static ObservableList<AVEquipment> selectedEquipment;


    public static ObservableList<AVEquipment> getReservation() {
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
        System.out.println("Reserve equipment initialized...");
        initEquipmentManager();
        initEquipmentTableView();
        initEquipmentColumns();
        updateEquipmentList();
        buildView();
        startTheParty();
    }


    private static void buildView() {

        lblHeader = new Label();
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        lblHeader.setText("Reserve Equipment");
        lblHeader.setTextAlignment(TextAlignment.CENTER);
        lblHeader.setAlignment(Pos.BOTTOM_CENTER);
        lblHeader.setPadding(new Insets(10, 0, 0, 0));

        mainVbox = new VBox(20);
        mainVbox.setAlignment(Pos.CENTER);


        btnConfirm = new Button("Confirm");
        btnCancel = new Button("Close");

        btnConfirm.setOnAction(actionEvent -> {
            if (AVAlert.confirmAction("Reserve Equipment", "Reserve all selected equipment?")) {

                selectedEquipment = tblEquipmentView.getSelectedHashItems();

                if (selectedEquipment == null || selectedEquipment.size() == 0 ) {
                    AVAlert.showMessage("No equipment", "No equipment selected.");
                }

                window.close();
            }
        });

        btnCancel.setOnAction(actionEvent -> {
            selectedEquipment = null;
            window.close();
        });

        if (selectedEquipment != null) {
            System.out.println("\n\n----SELECTED: " + selectedEquipment + "----\n\n");
            selectAllPreSelectedShit();
        }

        mainHBox = new HBox(50);
        mainHBox.getChildren().addAll(btnConfirm, btnCancel);
        mainHBox.setAlignment(Pos.TOP_CENTER);
        mainVbox.setPadding(new Insets(0, 50, 0, 50));
        mainVbox.getChildren().addAll(lblHeader, tblEquipmentView, mainHBox);
    }

    private static void initEquipmentTableView() {
        tblEquipmentView = new MultiSelectTableView<>();

        equipmentId = new TableColumn<AVEquipment, String>("ID");
        nameColumn = new TableColumn<AVEquipment, String>("NAME");
        typeColumn = new TableColumn<AVEquipment, String>("TYPE");
        brandColumn = new TableColumn<AVEquipment, String>("BRAND");
        serialNumberColumn = new TableColumn<>("SERIAL ID");
        locationColumn = new TableColumn<AVEquipment, String>("CURRENT LOCATION");
        isAvailableColumn = new TableColumn<AVEquipment, Boolean>("AVAILABLE");
        notesColumn= new TableColumn<AVEquipment, String>("NOTES");
    }

    private static void selectAllPreSelectedShit() {
        System.out.println("selectAllPreSelectedShit invoked... \nselectedEquipment size: " + selectedEquipment.size());
        tblEquipmentView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<AVEquipment> items = tblEquipmentView.getItems();
        HashSet<Integer> keys = new HashSet<>();

        for (int i = 0; i < selectedEquipment.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                System.out.println("COMPARING..: " + selectedEquipment.get(i).getName() + ":" + items.get(j).getName());
                if (selectedEquipment.get(i).equals(items.get(j))){
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

    private static void initEquipmentManager() {
        equipmentManager = new AVEquipmentManager();
    }

    public static void setPreSelectedEquipment(ObservableList<AVEquipment> preSelected, ObservableList<AVEquipment> preSelectedEquipment) {
        selectedEquipment = preSelectedEquipment;
    }

    private static ObservableList<AVEquipment> getEquipmentList() {
        ObservableList<AVEquipment> equipmentObservableList = FXCollections.observableArrayList();

        ArrayList<AVEquipment> equipmentEntries = equipmentManager.getAllEquipment();

        for (AVEquipment ave :
                equipmentEntries) {
            if (ave.isAvailable()) {
                equipmentObservableList.add(ave);
            }
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
        isAvailableColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isAvailable()));
        notesColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("notes"));

        tblEquipmentView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        isAvailableColumn.setCellFactory(col -> new TableCell<AVEquipment, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Yes" : "No");
            }
        });

        tblEquipmentView.getColumns().removeAll();
        tblEquipmentView.getColumns().clear();
        tblEquipmentView.getColumns().addAll(equipmentId,
                nameColumn,  typeColumn, brandColumn,
                serialNumberColumn, locationColumn, isAvailableColumn, notesColumn);

    }

    private static void updateEquipmentList() {
        tblEquipmentView.setItems(getEquipmentList());
        tblEquipmentView.getSortOrder().add(typeColumn);
    }
}
