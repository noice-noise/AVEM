package avem.fxml;

import avem.basic.AVEMInfo;
import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import avem.core.*;
import avem.developer.AdminTools;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static avem.basic.AVEMInfo.*;
import static avem.basic.AVEMTheme.*;
import static avem.core.AVEvent.*;


public class DashboardController implements Initializable {

    @FXML
    private AnchorPane pnlMainAnchorPane;
    @FXML
    private AnchorPane pnlHeader;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblWindowHeader;

    /* SIDEBAR */
    @FXML
    private StackPane pnlMainStack;
    @FXML
    private BorderPane pnlDashboard;
    @FXML
    private AnchorPane pnlEquipment;
    @FXML
    private AnchorPane pnlSettings;
    @FXML
    private AnchorPane pnlAccounts;

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnEquipment;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnAccounts;
    @FXML
    private Button btnSignOut;


    /* RIGHT-SIDE BULLETIN */
    @FXML
    private TextArea txtRecentActivity;
    @FXML
    private TextArea txtAnnouncement;


    /* TOP BAR */
    @FXML
    private Button btnReserve;
    @FXML
    private Button btnCheckout;


    /* EQUIPMENT TABLE */
    @FXML
    public TableView<AVEquipment> tblEquipmentView;
    @FXML
    private TableColumn<AVEquipment, String> equipmentId;
    @FXML
    private TableColumn<AVEquipment, String> nameColumn;
    @FXML
    private TableColumn<AVEquipment, String> typeColumn;
    @FXML
    private TableColumn<AVEquipment, String> brandColumn;
    @FXML
    private TableColumn<AVEquipment, String> serialNumberColumn;
    @FXML
    private TableColumn<AVEquipment, String> locationColumn;
    @FXML
    private TableColumn<AVEquipment, Boolean> isAvailableColumn;
    @FXML
    private TableColumn<AVEquipment, String> notesColumn;

    @FXML
    HBox hBoxEquipmentControls;
    @FXML
    private Button btnReportDefects;
    @FXML
    private Button btnAddEquipment;
    @FXML
    private Button btnViewEquipment;
    @FXML
    private ChoiceBox cbSort;


    /* CALENDAR */
    @FXML
    private Label lblMonthYear;
    @FXML
    private Label lblSunday;
    @FXML
    private Label lblMonday;
    @FXML
    private Label lblTuesday;
    @FXML
    private Label lblWednesday;
    @FXML
    private Label lblThursday;
    @FXML
    private Label lblFriday;
    @FXML
    private Label lblSaturday;
    @FXML
    private Button btnCalendarNext;
    @FXML
    private Button btnCalendarPrev;
    @FXML
    private VBox sundayVbox;
    @FXML
    private VBox mondayVbox;
    @FXML
    private VBox tuesdayVbox;
    @FXML
    private VBox wednesdayVbox;
    @FXML
    private VBox thursdayVbox;
    @FXML
    private VBox fridayVbox;
    @FXML
    private VBox saturdayVbox;
    @FXML
    private VBox vboxManageReservation;
    @FXML
    private Label lblReservationStatus;
    @FXML
    private ChoiceBox<String> cbMarkReservation;
    @FXML
    private AnchorPane pnReservationMark;

    private List<VBox> calendarVBoxes;
    private int vBoxCounter = 0;


    @FXML
    private ListView lstReservationInfo;
    @FXML
    private ListView lstEquipmentInfo;

    @FXML
    private Button btnManageAccounts;
    @FXML
    private Button btnEditProfile;
    @FXML
    private Button btnAnnouncements;
    @FXML
    private Button btnUserActivity;

    /* BUTTON IMAGE ICONS */
    @FXML
    private ImageView imgDashboard;
    @FXML
    private ImageView imgEquipment;
    @FXML
    private ImageView imgAccounts;
    @FXML
    private ImageView imgSettings;
    @FXML
    private ImageView imgSignOut;
    @FXML
    private ImageView imgReserve;
    @FXML
    private ImageView imgCheckInOut;
    @FXML
    private ImageView imgExit;

    /*  SETTINGS BUTTONS  */
    @FXML
    private Button btnAppearance;
    @FXML
    private Button btnInitSystem;
    @FXML
    private Button btnResetSystem;
    @FXML
    private Button btnFAQs;
    @FXML
    private AnchorPane pnlAppearance;
    @FXML
    private ChoiceBox<String> cbThemeMode;
    @FXML
    private ChoiceBox<String> cbThemeColor;
    @FXML
    private Button btnApplyTheme;
    @FXML
    private Button btnResetThemeDefaults;

    private LocalDate dateControl;
    private LocalDate sundayControl;
    private Month currentMonth;

    private AVEquipmentManager equipmentManager;
    private AVReservationManager reservationManager;

    private AVReservation selectedReservation;
    private HashMap<Integer, ArrayList<AVReservation>> entireWeekReservations;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static boolean isFirstDashboardVisit = true;
    private ArrayList<TableColumn> tblColList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initManagers();
        initEquipmentTable();
        initDashboard();

        updateBulletinPane();

        initAccessSettings();
        initCalendarGrid();
        initImageIcons();
        initNewSystemPrompt();
        initReservationStatusInfo();
        initThemeSettings();
    }

    private void initThemeSettings() {

        List<String> mode = getModeLabels();
        cbThemeMode.getItems().addAll(mode);
        cbThemeMode.getSelectionModel().select(AVEMTheme.getCurrentThemeMode());

        List<String> themes = getThemeLabels();
        cbThemeColor.getItems().addAll(themes);
        cbThemeColor.getSelectionModel().select(AVEMTheme.getCurrentThemeColor());
    }

    private void initReservationStatusInfo() {
        String[] eventStatusString = {
                ONGOING_STATUS,
                FINISHED_STATUS,
                CANCELLED_STATUS,
                RESERVED_STATUS,
                POSTPONED_STATUS};

        cbMarkReservation.getItems().addAll(Arrays.asList(eventStatusString));
        cbMarkReservation.setOnAction(actionEvent -> processMark());

        vboxManageReservation.setVisible(false);
    }

    private void processMark() {
        try {
            String selectedMark = cbMarkReservation.getSelectionModel().getSelectedItem();
            String operation = "";

            switch (selectedMark) {
                case RESERVED_STATUS:
                    operation = RESERVED_STATUS;
                    break;
                case ONGOING_STATUS:
                    operation = ONGOING_STATUS;
                    break;
                case CANCELLED_STATUS:
                    operation = CANCELLED_STATUS;
                    break;
                case POSTPONED_STATUS:
                    operation = POSTPONED_STATUS;
                    break;
                case FINISHED_STATUS:
                    operation = FINISHED_STATUS;
                    break;
                default:
                    throw new IllegalArgumentException("Selected Mark invalid.");
            }

            updateRecentActivity(CURRENT_ACCOUNT + " marked " +
                    selectedReservation.getEvent().getEventName() + ": " +
                    operation + ".");
            selectedReservation.getEvent().setEventStatus(operation);
            lblReservationStatus.setText(operation);
            reservationManager.updateReservationManagerFile();
            cbMarkReservation.getSelectionModel().clearSelection();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEquipmentTable() {
        initEquipmentColumns();
        updateEquipmentList();
        initSortChoiceBox();
    }

    private void initSortChoiceBox() {
        String[] sortString = {"ID", "Name", "Type", "Brand", "Serial No.", "Location", "Availability", "Notes"};
        tblColList = new ArrayList();
        tblColList.addAll(Arrays.asList(equipmentId, nameColumn, typeColumn, brandColumn, serialNumberColumn,
                locationColumn, isAvailableColumn, notesColumn));
        cbSort.getItems().addAll(Arrays.asList(sortString));
        cbSort.setOnAction(actionEvent -> processSort());
    }

    private void processSort() {
        int selectedIndex = cbSort.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) {
            tblEquipmentView.getSortOrder().clear();
            tblEquipmentView.getSortOrder().add(tblColList.get(selectedIndex));
        }
    }

    private void initNewSystemPrompt() {
        if (tblEquipmentView.getItems().size() == 0 && isFirstDashboardVisit) {
            AVAlert.showMessage("AVEM System", "Welcome to AVEM System!" +
                            "\nThere's no equipment available in the library yet. " +
                            "\nTo add equipment, go to \"Equipment\" found in the side bar." +
                            "\nTo initialize pre-coded list or reset everything just go to \"Settings\"." +
                            "\nThank you!",
                    300.0, 450.0);
            isFirstDashboardVisit = false;
        }
    }

    private void initImageIcons() {
        AVEMTheme.adjustImgThemeColor(Arrays.asList(imgDashboard, imgEquipment, imgAccounts, imgSettings,
                imgSignOut, imgReserve, imgCheckInOut, imgExit));
    }

    private void initCalendarGrid() {
        initDateControls();
        initCalendarLabels();
        updateCalendarReservations();
    }

    private void initDateControls() {
        dateControl = LocalDate.now();

        if (dateControl.getDayOfWeek() != DayOfWeek.SUNDAY) {
            dateControl = dateControl.minusDays(6);
            dateControl = getLastSunday(dateControl);
        }

        sundayControl = dateControl;
    }

    private void initCalendarLabels() {
        currentMonth = sundayControl.plusDays(6).getMonth();
        lblMonthYear.setText(currentMonth + " " + dateControl.getYear());
        dateControl = sundayControl;

        updateAllGridLabels();
    }

    private void initAccessSettings() {
        int accountAccess = CURRENT_ACCOUNT_ACCESS;
        btnSignOut.setText(CURRENT_ACCOUNT.getName());
        btnSignOut.setOnMouseEntered(mouseEvent -> btnSignOut.setText("Sign out"));
        btnSignOut.setOnMouseExited(mouseEvent -> btnSignOut.setText(CURRENT_ACCOUNT.getName()));

        if (accountAccess == AVAccount.ADMIN) {

        } else if (accountAccess == AVAccount.INTERNAL_STAFF) {
            //enter settings for internal staff here
        } else if (accountAccess == AVAccount.EXTERNAL_STAFF) {
            //enter settings for external staff here
        } else if (accountAccess == AVAccount.STUDENT_GUEST) {

            pnReservationMark.setVisible(false);
            btnReserve.setVisible(false);
            btnCheckout.setVisible(false);

            VBox vbSidebar = (VBox) btnAccounts.getParent();
            vbSidebar.getChildren().remove(btnAccounts);
            VBox parent = (VBox) btnInitSystem.getParent();
            parent.getChildren().remove(btnInitSystem);
            parent.getChildren().remove(btnResetSystem);

            hBoxEquipmentControls.setVisible(false);
        } else {
            System.out.println("ACCOUNT ACCESS NOT DEFINED");
        }
    }

    private void updateBulletinPane() {
        AVEMInfo.initialize();
        txtAnnouncement.setWrapText(true);
        txtRecentActivity.setWrapText(true);
        txtRecentActivity.setEditable(false);
        txtAnnouncement.setEditable(false);
        txtRecentActivity.appendText(RECENT_ACTIVITY);
        txtAnnouncement.appendText(ANNOUNCEMENT);
    }

    private void initDashboard() {
        pnlDashboard.toFront();
        btnDashboard.fire();
    }

    private void initManagers() {
        equipmentManager = new AVEquipmentManager();
        reservationManager = new AVReservationManager();
        entireWeekReservations = new HashMap<>();
    }

    public void initEquipmentColumns() {
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
                setText(empty ? null : item ? "Available" : "Reserved");
            }
        });
    }

    private void updateCalendarReservations() {
        LocalDate ldStart = sundayControl;
        LocalDate ldEnd = sundayControl.plusDays(6);
        LocalDate ldLimit = sundayControl.plusDays(7);

        String m = String.valueOf(ldStart.getMonthValue());
        String d = String.valueOf(ldStart.getDayOfMonth());
        String y = String.valueOf(ldStart.getYear());

        AVDate start = new AVDate(m, d, y);

        AVDate end = new AVDate(String.valueOf(ldEnd.getMonthValue()),
                String.valueOf(ldEnd.getDayOfMonth()),
                String.valueOf(ldEnd.getYear()));

        AVDate limit = new AVDate(String.valueOf(ldLimit.getMonthValue()),
                String.valueOf(ldLimit.getDayOfMonth()),
                String.valueOf(ldLimit.getYear()));

        resetCalendar();
        ArrayList<AVReservation> parsedReservations = new ArrayList<>();
        AVDate dateCounter = start;


        while (!dateCounter.equals(limit)) {
            parsedReservations = reservationManager.getSortedReservationsFor(dateCounter);
            if (parsedReservations.size() > 0) {
                createBoxesFor(parsedReservations);
            }

            LocalDate newDate = LocalDate.of(Integer.parseInt(dateCounter.getYear()),
                    Integer.parseInt(dateCounter.getMonth()),
                    Integer.parseInt(dateCounter.getDay()));

            newDate = newDate.plusDays(1);
            String mo = String.valueOf(newDate.getMonthValue());
            String da = String.valueOf(newDate.getDayOfMonth());
            String ye = String.valueOf(newDate.getYear());
            dateCounter = new AVDate(mo, da, ye);

            vBoxCounter++;
        }

    }

    private void resetCalendar() {

        sundayVbox.getChildren().clear();
        mondayVbox.getChildren().clear();
        tuesdayVbox.getChildren().clear();
        wednesdayVbox.getChildren().clear();
        thursdayVbox.getChildren().clear();
        fridayVbox.getChildren().clear();
        saturdayVbox.getChildren().clear();

        calendarVBoxes = new ArrayList<VBox>();
        calendarVBoxes.add(sundayVbox);
        calendarVBoxes.add(mondayVbox);
        calendarVBoxes.add(tuesdayVbox);
        calendarVBoxes.add(wednesdayVbox);
        calendarVBoxes.add(thursdayVbox);
        calendarVBoxes.add(fridayVbox);
        calendarVBoxes.add(saturdayVbox);
        vBoxCounter = 0;
    }

    private void createBoxesFor(ArrayList<AVReservation> anEntireDayReservation) {
        for (AVReservation aReservation :
                anEntireDayReservation) {
            createReservationBox(aReservation);
        }
    }

    private void updateAllGridLabels() {
        updateGridLabel(lblSunday);
        updateGridLabel(lblMonday);
        updateGridLabel(lblTuesday);
        updateGridLabel(lblWednesday);
        updateGridLabel(lblThursday);
        updateGridLabel(lblFriday);
        updateGridLabel(lblSaturday);
    }

    private void updateGridLabel(Label gdLabel) {
        if (dateControl.getMonth() != currentMonth) {
            gdLabel.setOpacity(0.3);
        } else {
            gdLabel.setOpacity(1);
        }
        gdLabel.setText(String.valueOf(dateControl.getDayOfMonth()));
        dateControl = dateControl.plusDays(1);
    }

    private LocalDate getLastSunday(LocalDate localDate) {

        while (localDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            localDate = localDate.plusDays(1);
        }

        return localDate;
    }

    private void createReservationBox(AVReservation reservation) {

        String rLabel = reservation.getEvent().getEventName();

        AVButton button = new AVButton(rLabel, reservation);

        button.setMaxWidth(100);
        button.setMaxHeight(400);
        button.setWrapText(true);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setId("av-activity");

        button.setOnAction(actionEvent ->
                processSelectedReservation(button.getReservation())
        );

        Tooltip tooltip = new Tooltip(reservation.getEvent().getEventStartTime() + " - "
                + reservation.getEvent().getEventEndTime());
        button.setTooltip(tooltip);

        calendarVBoxes.get(vBoxCounter).setSpacing(10);
        calendarVBoxes.get(vBoxCounter).getChildren().add(button);
    }

    private void processSelectedReservation(AVReservation res) {
        selectedReservation = res;
        vboxManageReservation.setVisible(true);
        lblReservationStatus.setText(res.getEvent().getEventStatus());

        lstReservationInfo.getItems().clear();
        lstReservationInfo.getItems().add("Date: \t" + res.getEvent().getEventDate().toString());
        lstReservationInfo.getItems().add("Event: \t" + res.getEvent().getEventName());
        lstReservationInfo.getItems().add("Time: \t" + res.getEvent().getEventStartTime() +
                " - " + res.getEvent().getEventEndTime());
        lstReservationInfo.getItems().add("Venue: \t" + res.getEvent().getEventVenue());
        lstReservationInfo.getItems().add("Department: \t" + res.getEvent().getEventDepartment());
        lstReservationInfo.getItems().add("Contact Person: \t" + res.getEvent().getContactPerson());

        updateEquipmentListView(res);
    }

    private void updateEquipmentListView(AVReservation res) {
        lstEquipmentInfo.getItems().clear();
        lstEquipmentInfo.getItems().add("RESERVED EQUIPMENT: ");
        lstEquipmentInfo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstEquipmentInfo.getItems().add("Status: " + res.getEquipmentStatus());

        String eqLabel = "";

        HashMap<String, AVEquipment> eq = res.getEquipmentBundle();
        for (AVEquipment av :
                eq.values()) {
            eqLabel = "(" + av.getType() + ") " + av.getName();
            lstEquipmentInfo.getItems().add(eqLabel);
        }
    }

    @FXML
    public void handleButton(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnDashboard) {
            handleSpecialNav(btnDashboard);
            pnlDashboard.toFront();
        } else if (actionEvent.getSource() == btnAccounts) {
            handleSpecialNav(btnAccounts);
            pnlAccounts.toFront();
        } else if (actionEvent.getSource() == btnSettings) {
            System.out.println(pnlSettings.getStyle());
            handleSpecialNav(btnSettings);
            pnlSettings.toFront();
        } else if (actionEvent.getSource() == btnEquipment) {
            handleSpecialNav(btnEquipment);
            updateEquipmentList();
            pnlEquipment.toFront();
        } else if (actionEvent.getSource() == btnExit) {
            handleExit();
        } else if (actionEvent.getSource() == btnReportDefects) {
            processDefectReport();
        } else if (actionEvent.getSource() == btnAddEquipment) {
            displayAddEquipment();
        } else if (actionEvent.getSource() == btnViewEquipment) {
            viewSelectedEquipment();
        } else if (actionEvent.getSource() == btnCalendarNext) {
            sundayControl = sundayControl.plusDays(7);
            initCalendarLabels();
            updateCalendarReservations();
        } else if (actionEvent.getSource() == btnCalendarPrev) {
            sundayControl = sundayControl.minusDays(7);
            initCalendarLabels();
            updateCalendarReservations();
        } else if (actionEvent.getSource() == btnAppearance) {
            processAppearanceAction();
        } else if (actionEvent.getSource() == btnFAQs) {
            AVEMInfo.displayFAQs();
        } else if (actionEvent.getSource() == btnApplyTheme) {

            int selectedMode = cbThemeMode.getSelectionModel().getSelectedIndex();
            int selectedColor = cbThemeColor.getSelectionModel().getSelectedIndex();

            if (selectedMode != -1 && selectedColor != -1) {
                applyThemeSetting();
                switchToRefreshPage();
            } else {
                AVAlert.showMessage("Error", "Please try again.");
            }
        } else if (actionEvent.getSource() == btnResetThemeDefaults) {
            resetThemeSettings();
            switchToRefreshPage();
        }
    }

    private void resetThemeSettings() {
        setTheme(DARK_MODE_VALUE, MAROON_GOLD);
    }

    private void applyThemeSetting() throws IOException {
        int i = cbThemeMode.getSelectionModel().getSelectedIndex();
        int j = cbThemeColor.getSelectionModel().getSelectedIndex();
        setTheme(i, j);
    }

    private void processAppearanceAction() throws IOException {
        pnlAppearance.toFront();
    }

    private void processDefectReport() {

        AVEquipment selectedAVEquipment = tblEquipmentView.getSelectionModel().getSelectedItem();
        String defectReport = "";
        if (selectedAVEquipment == null) {
            AVAlert.showMessage("Select", "Please select an equipment first.");
        } else {
            try {
                defectReport = EquipmentView.getDefectReport();

                if (defectReport != null && defectReport.trim().length() != 0) {
                    selectedAVEquipment.appendNotes(defectReport);

                    equipmentManager.updateEquipmentManager();
                    tblEquipmentView.refresh();
                    AVAlert.showMessage("Successful", "Defect Report has been submitted.\nEquipment list has been updated successfully.");
                    AVEMInfo.updateRecentActivity(CURRENT_ACCOUNT + " reported a defect on: " + selectedAVEquipment.getName() + " equipment.");
                }
            } catch (NullPointerException ne) {
                // operation cancelled
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void viewSelectedEquipment() {
        try {
            AVEquipment selectedEquipment = tblEquipmentView.getSelectionModel().getSelectedItem();
            if (selectedEquipment == null) {
                AVAlert.showMessage("Select", "Please an equipment to view.");
            } else {
                EquipmentView.view(selectedEquipment);
            }
        } catch (Exception e) {
            // operation cancelled
        }
    }

    @FXML
    public void handleAccountButtons(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnManageAccounts) {
            displayAccountView();
        } else if (actionEvent.getSource() == btnEditProfile) {
            if (CURRENT_ACCOUNT.getUsername().equals("admin")) {
                AVAlert.showMessage("Admin Privilege", "Sorry, Super-Admin\naccount cannot be modified.");
            } else {
                AVAccount edited = AccountView.displayEdit(CURRENT_ACCOUNT);
                updateRecentActivity(CURRENT_ACCOUNT + " updated profile.");
                AVEMInfo.editAccount(CURRENT_ACCOUNT, edited);
            }
        } else if (actionEvent.getSource() == btnUserActivity) {
            AVEMInfo.displayRecentActivity();

        } else if (actionEvent.getSource() == btnAnnouncements) {
            AVEMInfo.displayAnnouncement();
            updateBulletinPane();
        }
    }

    private void displayAccountView() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AccountsView.fxml"));
        Stage window = new Stage();
        window.initStyle(StageStyle.TRANSPARENT);
        window.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());

        window.setScene(scene);
        window.setTitle(VERSION);
        AVEMWindows.makeMovable(window, root);
        window.showAndWait();
        window.centerOnScreen();
        updateBulletinPane();
    }

    private void handleSpecialNav(Button btnNav) {
        String def = "-fx-background-color: -fx-primary";
        String specialNav = "-fx-background-color: -fx-gold; -fx-font-size: 20px; -fx-background-insets: 0 0 0 0; -fx-font-weight: BOLD";

        btnNav.getGraphic().setStyle("-fx-fill-color: white");
        btnDashboard.setStyle(def);
        btnEquipment.setStyle(def);
        btnAccounts.setStyle(def);
        btnSettings.setStyle(def);

        if (btnNav == btnDashboard) {
            btnDashboard.setStyle(specialNav);
        } else if (btnNav == btnEquipment) {
            btnEquipment.setStyle(specialNav);
        } else if (btnNav == btnAccounts) {
            btnAccounts.setStyle(specialNav);
        } else if (btnNav == btnSettings) {
            btnSettings.setStyle(specialNav);
        }
    }

    public void handleExit() {
        boolean ok = AVAlert.confirmAction("Exiting AVEM", "Are you sure you want to quit?");
        if (ok) {
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.close();
        }
    }

    private void displayAddEquipment() {
        AVEquipment avEquipment = EquipmentView.displayAdd();
        if (avEquipment == null) {
            System.out.println("Add equipment cancelled.");
        } else {
            System.out.println("Add equipment success.");
            equipmentManager.addEquipment(avEquipment);
            updateRecentActivity(CURRENT_ACCOUNT + " added " + avEquipment.getName() + " equipment.");
            refreshEquipmentTable();
        }
    }

    private void updateRecentActivity(String activity) {
        AVEMInfo.updateRecentActivity(activity);
        txtRecentActivity.setText("");
        txtRecentActivity.appendText(getRecentActivity());
    }

    private void updateAnnouncement(String newAnnouncement) {
        AVEMInfo.updateAnnouncement(newAnnouncement);
        txtAnnouncement.setText("");
        txtAnnouncement.appendText(getAnnouncement());
    }

    public void editEquipmentTableCell(ActionEvent actionEvent) {

        try {
            AVEquipment selectedAVEquipment = tblEquipmentView.getSelectionModel().getSelectedItem();
            if (selectedAVEquipment == null) {
                AVAlert.showMessage("Select Equipment", "Please select an equipment to edit.");
            } else {
                AVEquipment newAVE = EquipmentView.displayEdit(selectedAVEquipment);
                if (newAVE != null) {
                    equipmentManager.editEquipment(selectedAVEquipment, newAVE);
                    refreshEquipmentTable();
                    updateEquipmentList();
                    updateRecentActivity(CURRENT_ACCOUNT + " edited " + selectedAVEquipment.getName() + " equipment.");
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
            AVAlert.showMessage("Select Equipment", "Please select an equipment to edit.");
        } catch (IllegalArgumentException ia) {
            AVAlert.showMessage("Ooops.", "Please try again.");
        } catch (Exception e) {
            AVAlert.showMessage("Error", e.toString());
            e.printStackTrace();
        }
    }

    @FXML
    public void removeEquipment() {

        try {
            tblEquipmentView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            AVEquipment selectedAVEquipment = tblEquipmentView.getSelectionModel().getSelectedItem();
            if (selectedAVEquipment == null) {

                AVAlert.showMessage("Select Equipment",
                        "Please select an equipment to remove.");

            } else if (AVAlert.confirmAction("Confirm",
                    "Remove \n\"" + selectedAVEquipment.getName() +
                            "\" equipment?\n\n You can't undo this process.")) {

                ObservableList<AVEquipment> selectedRows, allTableAVEquipment;
                allTableAVEquipment = tblEquipmentView.getItems();
                selectedRows = tblEquipmentView.getSelectionModel().getSelectedItems();

                if (selectedRows != null && selectedRows.size() != 0) {
                    for (AVEquipment ave :
                            selectedRows) {
                        allTableAVEquipment.remove(ave);
                        equipmentManager.removeEquipment(ave);
                        updateRecentActivity(CURRENT_ACCOUNT + " removed " + ave.getName() + " equipment.");

                        if (selectedRows.size() == 0) {
                            break;
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void adminToolsInit() throws IOException {
        boolean yes = AVAlert.confirmAction("Notice on Initialization", "" +
                "Initializing the AVEM system\nis recommended only if: " +
                "\n1. Old data and files are not needed." +
                "\n2. Installing in a new computer system." +
                "\n3. The system is subject for full reset." +
                "\nPress Yes if this you still wish to proceed.");
        if (yes) {
            System.out.println("Admin init ran.");
            AdminTools.createInitialEquipmentFiles();
            AdminTools.createInitialBulletinBoard();
            AdminTools.createInitialAccounts();

            updateRecentActivity(CURRENT_ACCOUNT + " initialized the AVEM system.");
            refreshEquipmentTable();
            switchToRefreshPage();
        }
    }

    @FXML
    public void resetEverything() throws IOException {

        boolean yes = AVAlert.confirmAction("Notice on Full System Reset", "" +
                "Resetting the AVEM system\nis recommended only if: " +
                "\n1. Old data and files are not needed." +
                "\n2. Installing in a new computer system." +
                "\n3. The system is subject for full reset." +
                "\nPress Yes if this you still wish to proceed.");

        if (yes) {
            equipmentManager.resetAllFilesAndDirectory();
            reservationManager.resetAllFilesAndDirectory();

            AVEMInfo.resetBulletin();
            AVEMInfo.resetAccounts();

            switchToRefreshPage();
            setFirstAccessAsTrue();
        }
    }

    @FXML
    public void refreshEquipmentTable() {
        tblEquipmentView.getItems().clear();
        tblEquipmentView.refresh();

        initEquipmentTable();
    }

    private ObservableList<AVEquipment> getEquipmentList() {
        ObservableList<AVEquipment> equipmentObservableList = FXCollections.observableArrayList();
        ArrayList<AVEquipment> equipmentEntries = equipmentManager.getAllEquipment();

        for (AVEquipment ave :
                equipmentEntries) {
            equipmentObservableList.add(ave);
        }

        return equipmentObservableList;
    }

    private void updateEquipmentList() {
        tblEquipmentView.setItems(getEquipmentList());
    }

    public void switchToReservePane(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ReserveView.fxml"));

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

    public void switchToCheckOut(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CheckInOutPage.fxml"));
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

    @FXML
    private void switchToSignOut() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SigningPage.fxml"));
        Stage window = (Stage) ((Node) btnSignOut).getScene().getWindow();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        window.setScene(scene);
        window.setTitle(VERSION);
        AVEMWindows.makeMovable(window, root);
        window.show();
        window.centerOnScreen();
    }

    @FXML
    private void switchToRefreshPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RefreshPage.fxml"));
        Stage refreshWindow = new Stage();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        refreshWindow.initStyle(StageStyle.TRANSPARENT);
        refreshWindow.resizableProperty().setValue(false);
        refreshWindow.setScene(scene);
        refreshWindow.setTitle(VERSION);
        AVEMWindows.makeMovable(refreshWindow, root);

        Stage currentWindow = (Stage) btnExit.getScene().getWindow();
        currentWindow.close();

        refreshWindow.show();
        refreshWindow.centerOnScreen();
    }
}
