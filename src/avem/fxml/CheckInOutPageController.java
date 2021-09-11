package avem.fxml;

import avem.basic.AVEMInfo;
import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import avem.core.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import static avem.basic.AVEMInfo.*;
import static avem.basic.AVEMInfo.CURRENT_ACCOUNT;
import static avem.basic.AVEMTheme.VERSION;

public class CheckInOutPageController implements Initializable {

    @FXML
    private TextArea txtRecentActivity;

    @FXML
    private TextArea txtAnnouncement;

    @FXML
    private Button btnBack;


    @FXML
    private Button btnExit;

    @FXML
    private Label lblActivity;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblVenue;
    @FXML
    private Label lblDepartment;
    @FXML
    private Label lblContact;
    @FXML
    private Label lblNumber;
    @FXML
    private Label lblApproved;
    @FXML
    private Label lblEncoded;
    @FXML
    private Label lblNotes;

    @FXML
    private ListView lwEquipment;

    @FXML
    private ChoiceBox<String> cbViewMode;

    @FXML
    private Button btnCheckIn;
    @FXML
    private Button btnCheckOut;

    @FXML
    private VBox vboxReservations;

    @FXML
    private ImageView imgBack;
    @FXML
    private ImageView imgExit;


    private AVReservationManager reservationManager;
    private AVEquipmentManager equipmentManager;
    private HashMap<String, AVEquipment> equipmentBundle;
    private AVReservation selectedReservation;
    private LocalDate startDate;
    private LocalDate endDate;


    public void goBack() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage window = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        window.setScene(scene);
        window.setTitle(VERSION);
        AVEMWindows.makeMovable(window, root);
        window.show();
        window.centerOnScreen();
    }

    public void handleExit() {
        boolean ok = AVAlert.confirmAction("Exiting AVEM", "Are you sure you want to quit?");
        if (ok) {
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.close();
        }
    }

    public void handleButton(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnBack) {
            goBack();
        } else if (actionEvent.getSource() == btnExit) {
            handleExit();
        } else if (actionEvent.getSource() == btnCheckIn) {
            if (selectedReservation == null) {
                AVAlert.showMessage("Select", "Please select a reservation.");
            } else if (wasCheckedIn()) {
                AVAlert.showMessage("Notice", "Selected Reservation\nalready checked in.");
            } else if (isReserved()) {
                AVAlert.showMessage("Notice", "Reservation must be checked out first.");
            }
            else if (AVAlert.confirmAction("Checking in", "Checking in reserved equipment means that\n" +
                    " all the equipment has been returned\nand in good condition.\n\n" +
                    "Contact Person: " + selectedReservation.getEvent().getContactPerson() +
                    "\nCurrent Account: " + CURRENT_ACCOUNT.getName())) {

                checkInEquipment();
            }
        } else if (actionEvent.getSource() == btnCheckOut) {
            if (selectedReservation == null) {
                AVAlert.showMessage("Select", "Please select a reservation.");
            } else if (wasCheckedOut()) {
                boolean yes = AVAlert.confirmAction("Checked out", "Equipment already checked out.\nUpdate check out status?");
                if (yes) {

                    checkOutEquipment();
                }
            }
            else if (AVAlert.confirmAction("Checking out", "Checking out reserved equipment means that\n" +
                    "the contact person will be responsible\nof the equipment listed.\n\n" +
                    "Contact Person: " + selectedReservation.getEvent().getContactPerson() +
                    "\nCurrent Account: " + CURRENT_ACCOUNT.getName())) {

                processCheckOut();
                runCheckOutUpdates();
            }
        }
    }

    private void checkInEquipment() {
        processCheckIn();
        runCheckInUpdates();
    }

    private void checkOutEquipment() {
        processCheckOut();
        runCheckOutUpdates();
    }

    private void runCheckInUpdates() {
        initReservationVBox();
        updateEquipmentListView(selectedReservation);
        updateRecentActivity(CURRENT_ACCOUNT + " checked in equipment for: " +
                selectedReservation.getEvent().getEventName() + ".");
        initBulletinPane();
    }

    private void runCheckOutUpdates() {
        initReservationVBox();
        updateEquipmentListView(selectedReservation);
        updateRecentActivity(CURRENT_ACCOUNT + " checked out equipment for: " +
                selectedReservation.getEvent().getEventName() + ".");
        initBulletinPane();
    }

    private boolean isReserved() {
        return selectedReservation.getEquipmentStatus().equals(AVReservation.RESERVED);
    }

    private boolean wasCheckedOut() {
        return selectedReservation.getEquipmentStatus().equals(AVReservation.CHECKED_OUT);
    }

    private boolean wasCheckedIn() {
        boolean wasCheckedIn = selectedReservation.getEquipmentStatus().equals(AVReservation.CHECKED_IN);
        return wasCheckedIn;
    }


    private void updateRecentActivity(String activity) {
        AVEMInfo.updateRecentActivity(activity);
        txtRecentActivity.setText("");
        txtRecentActivity.appendText(getRecentActivity());
    }

    private void processCheckOut() {
        try {

            ObservableList<AVEquipment> eqBundle = CheckInOutEquipment.viewEquipmentBundle(selectedReservation);
            selectedReservation = CheckInOutEquipment.getReservation();
            reservationManager.updateReservationManagerFile();

            if (eqBundle != null) {
                selectedReservation.setEquipmentStatus(AVReservation.CHECKED_OUT);
                reservationManager.setCheckedOut(selectedReservation);

                for (AVEquipment ave :
                        eqBundle) {
                    ave.setAvailable(false);
                }

                for (AVEquipment ave :
                        eqBundle) {
                    equipmentManager.getEquipment(ave).setAvailable(false);
                    equipmentManager.getEquipment(ave).setCurrentLocation(selectedReservation.getEvent().getEventVenue());
                    equipmentManager.updateEquipmentManager();
                }

            }

        } catch (NullPointerException ne) {
            //function cancelled.
            ne.printStackTrace();
        } catch (Exception e) {
            AVAlert.showMessage("Error", "An error occurred. Please try again.");
        }

    }

    private void processCheckIn() {

        ObservableList<AVEquipment> eqBundle = CheckInOutEquipment.viewEquipmentBundle(selectedReservation);
        selectedReservation = CheckInOutEquipment.getReservation();
        reservationManager.updateReservationManagerFile();

        if (eqBundle != null) {
            selectedReservation.setEquipmentStatus(AVReservation.CHECKED_IN);
            reservationManager.setCheckedIn(selectedReservation);

            for (AVEquipment ave :
                    eqBundle) {
                ave.setAvailable(true);
            }

            for (AVEquipment ave :
                    eqBundle) {
                equipmentManager.getEquipment(ave).setAvailable(true);
                equipmentManager.getEquipment(ave).setCurrentLocation("Main Storage");
                equipmentManager.updateEquipmentManager();
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWeekDateRange();
        initManager();
        initReservationVBox();
        initBulletinPane();
        initImageIcons();
        initViewModeChoiceBox();
    }

    private void initViewModeChoiceBox() {
        cbViewMode.getItems().add("This Week");
        cbViewMode.getItems().add("This Month");
        cbViewMode.getItems().add("All Reservations");
        cbViewMode.getSelectionModel().selectFirst();
        cbViewMode.toFront();


        cbViewMode.setOnAction(actionEvent -> handleViewMode());
    }

    private void handleViewMode() {
        if (cbViewMode.getSelectionModel().getSelectedIndex() == 0) {
            setWeekDateRange();
        } else if (cbViewMode.getSelectionModel().getSelectedIndex() == 1) {
            setMonthDateRange();
        } else if (cbViewMode.getSelectionModel().getSelectedIndex() == 2) {
            setAllDateRange();
        }
        initReservationVBox();
    }

    private void initImageIcons() {
        AVEMTheme.adjustImgThemeColor(Arrays.asList(imgBack, imgExit));
    }

    private void initReservationVBox() {
        vboxReservations.getChildren().clear();

        AVDate dayCounter = new AVDate(String.valueOf(startDate.getMonthValue()),
                String.valueOf(startDate.getDayOfMonth()),
                String.valueOf(startDate.getYear()));


        AVDate dateLimit = new AVDate(String.valueOf(endDate.getMonthValue()),
                String.valueOf(endDate.getDayOfMonth()),
                String.valueOf(endDate.getYear()));

        while (!dayCounter.equals(dateLimit)) {

            ArrayList<AVReservation> r = reservationManager.getSortedReservationsFor(dayCounter);
            createReservationBoxes(r);

            LocalDate newDate = LocalDate.of(Integer.valueOf(dayCounter.getYear()),
                    Integer.valueOf(dayCounter.getMonth()), Integer.valueOf(dayCounter.getDay()));
            newDate = newDate.plusDays(1);
            dayCounter = new AVDate(String.valueOf(newDate.getMonthValue()),
                    String.valueOf(newDate.getDayOfMonth()),
                    String.valueOf(newDate.getYear()));
        }

    }

    private void setWeekDateRange() {
        startDate = LocalDate.now();
        startDate = startDate.minusDays(7);

        endDate = LocalDate.now();
        endDate = endDate.plusDays(7);
    }

    private void setMonthDateRange() {
        startDate = LocalDate.now();
        startDate = startDate.minusMonths(1);

        endDate = LocalDate.now();
        endDate = endDate.plusMonths(1);
    }

    private void setAllDateRange() {
        startDate = LocalDate.now();
        startDate = startDate.minusYears(10);

        endDate = LocalDate.now();
        endDate = endDate.plusYears(10);
    }

    private void createReservationBoxes(ArrayList<AVReservation> reservations) {
        for (AVReservation r :
                reservations) {
            createBox(r);
        }
    }

    private void createBox(AVReservation r) {

        String rLabel =
                r.getEvent().getEventName() + " (" +
                        r.getEvent().getEventStatus() + ") \n" +
                        r.getEvent().getEventDate() + "" +
                        "     " + r.getEvent().getEventStartTime() + " – " +
                        r.getEvent().getEventEndTime() + "\n" +
                        "Equipment  " + "  " + r.getEquipmentStatus() + "\n";


        AVButton button = new AVButton(rLabel, r);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(100);
        button.setAlignment(Pos.BASELINE_LEFT);
        button.setTextAlignment(TextAlignment.LEFT);
        button.setPadding(new Insets(20, 10, 10, 30));
        button.setStyle("-fx-font-size: 14");
        button.setOnAction(actionEvent ->
                        processSelectedReservation(button.getReservation())
        );

        vboxReservations.getChildren().add(button);
    }

    private void processSelectedReservation(AVReservation res) {
        selectedReservation = res;

        lblDate.setText(res.getEvent().getEventDate().toString());
        lblActivity.setText(res.getEvent().getEventName());
        lblTime.setText(res.getEvent().getEventStartTime() +
                " - " + res.getEvent().getEventEndTime());
        lblVenue.setText(res.getEvent().getEventVenue());
        lblDepartment.setText(res.getEvent().getEventDepartment());
        lblContact.setText(res.getEvent().getContactPerson());
        lblNumber.setText(res.getEvent().getContactNumber());
        lblApproved.setText(res.getEvent().getApprovedBy());
        lblEncoded.setText(res.getEvent().getEncodedBy());
        lblNotes.setText(res.getEvent().getAdditionalNotes());

        updateEquipmentListView(res);
    }

    private void updateEquipmentListView(AVReservation res) {
        lwEquipment.getItems().clear();
        lwEquipment.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lwEquipment.getItems().add("Status: " + res.getEquipmentStatus());

        String eqLabel = "";

        HashMap<String, AVEquipment> eq = res.getEquipmentBundle();
        for (AVEquipment av :
                eq.values()) {
            eqLabel = "(" + av.getType() + ") " + av.getName();
            lwEquipment.getItems().add(eqLabel);
        }
    }

    private void initManager() {
        equipmentManager = new AVEquipmentManager();
        reservationManager = new AVReservationManager();
        selectedReservation = null;
    }

    private void initBulletinPane() {
        txtAnnouncement.setWrapText(true);
        txtRecentActivity.setWrapText(true);
        txtRecentActivity.setEditable(false);
        txtAnnouncement.setEditable(false);
        txtRecentActivity.appendText(RECENT_ACTIVITY);
        txtAnnouncement.appendText(ANNOUNCEMENT);
    }



}
