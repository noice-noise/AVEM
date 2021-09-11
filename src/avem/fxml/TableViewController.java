package avem.fxml;

import avem.basic.AVEMTheme;
import avem.core.AVEquipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static avem.basic.AVEMTheme.VERSION;

public class TableViewController implements Initializable {

    @FXML
    private Label lblHeader;
    @FXML
    private Button btnCancel;
    @FXML
    private TableView<AVEquipment> tableView;
    @FXML
    private TableColumn<AVEquipment, String> nameColumn;
    @FXML
    private TableColumn<AVEquipment, String> typeColumn;
    @FXML
    private TableColumn<AVEquipment, String> serialNumberColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("type"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("serialNumber"));

        tableView.setItems(getEquipmentList());
    }


    public void switchToDashboard(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        window.setScene(scene);
        window.setTitle(VERSION);
        window.show();
    }

    /**
     *
     * @return Observable List of Equipments
     */
    public ObservableList<AVEquipment> getEquipmentList() {
        ObservableList<AVEquipment> equipmentObservableList = FXCollections.observableArrayList();
        equipmentObservableList.add(new AVEquipment("Sony", "Camera", "SN1200"));
        equipmentObservableList.add(new AVEquipment("Lenovo 330", "Laptop", "SN1220"));
        equipmentObservableList.add(new AVEquipment("Benson", "Tripod", "SQC1355"));

        return equipmentObservableList;
    }
}

