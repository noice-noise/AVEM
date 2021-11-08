package avem.fxml;


import avem.basic.AVEMInfo;
import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import avem.core.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.*;
import java.util.HashMap;
import java.util.ResourceBundle;

import static avem.basic.AVEMInfo.*;
import static avem.basic.AVEMTheme.VERSION;

public class ReserveViewController implements Initializable {

    /* EPIC BUTTONS GRID */
    @FXML
    public Button btnBack;
    @FXML
    public Button btnExit;
    @FXML
    public Button btnClear;
    @FXML
    public Button btnVerifyEvent;
    @FXML
    public Button btnReserve;
    @FXML
    public Button btnViewEquipment;

    /* BUTTON IMAGE ICONS */
    @FXML
    private ImageView imgBack;
    @FXML
    private ImageView imgExit;
    @FXML
    private ImageView imgClearInputs;

    /* RESERVATION GRID */
    @FXML
    private ChoiceBox<String> venueChoiceBox;
    @FXML
    private ChoiceBox<String> departmentChoiceBox;
    @FXML
    private ChoiceBox<String> startTimeChoiceBox;
    @FXML
    private ChoiceBox<String> endTimeChoiceBox;
    @FXML
    private ChoiceBox<String> startTimeOfDay;
    @FXML
    private ChoiceBox<String> endTimeOfDay;
    @FXML
    private TextField txtActivity;
    @FXML
    private TextField txtApprovedBy;
    @FXML
    private TextField txtContactPerson;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField txtContactNumber;
    @FXML
    private TextArea txtAreaNotes;
    @FXML
    private ChoiceBox<String> templateChoiceBox;


    /* EQUIPMENT TABLE VIEW */
    @FXML
    public TableView<AVEquipment> tblEquipmentVIew;
    @FXML
    private TableColumn<AVEquipment, String> nameColumn;
    @FXML
    private TableColumn<AVEquipment, String> typeColumn;
    @FXML
    private TableColumn<AVEquipment, String> brandColumn;
    @FXML
    private TableColumn<AVEquipment, String> notesColumn;

    /* BULLETIN PANE */
    @FXML
    private TextArea txtAnnouncement;
    @FXML
    private TextArea txtRecentActivity;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private DateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");
    private DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    private AVEquipmentManager equipmentManager;
    private AVReservationManager reservationManager;


    String[] venueContents = new String[]{
            "Gym", "Chapel", "Conference Room", "Case Room",
            "Quadrangle", "SHS Quadrangle", "Executive Lobby", "Accounting Lobby", "SHS Court",
            "Engineering Building", "Back Gate", "Front Gate", "Classroom", "Out-campus", "In-campus"
    };

    String[] departmentContents = new String[]{
            "Basic Education", "SHS", "Accounting",
            "OAS", "SSD", "ETO", "SSG", "TSG", "RDCO", "ITSO", "MSDO",
            "College Faculty", "SAO", "Executive Office", "Office of the President",
            "Office of the Vice President", "Registrar", "PE", "PCO", "Student/Guest", "Others"
    };

    String[] timeContents = new String[]{
            "1:00", "1:30", "2:00", "2:30",
            "3:00", "3:30", "4:00", "4:30", "5:00", "5:30",
            "6:00", "6:30", "7:00", "7:30", "8:00", "8:30",
            "9:00", "9:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30"
    };

    String[] templateContents = new String[]{"Weekly Mass", "Board of Directors Meeting",
            "Seminar Documentation", "Student Request"};


    private ObservableList<AVEquipment> toReserveEquipment;
    private AVReservation reservation;
    private AVEvent event;
    private HashMap<String, AVEquipment> equipmentBundle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChoiceBoxes();
        initInputAction();
        initDatePickerFormat();
        initBulletinPane();
        initManagers();
        initImageIcons();
    }

    private void initImageIcons() {
        AVEMTheme.adjustImgThemeColor(Arrays.asList(imgBack, imgExit, imgClearInputs));
    }

    private void initManagers() {
        equipmentManager = new AVEquipmentManager();
        reservationManager = new AVReservationManager();

        reservation = null;
        event = null;
        equipmentBundle = null;
    }

    private void initInputAction() {
        try {
            datePicker.setOnAction(actionEvent -> verifyPastDate());
            templateChoiceBox.setOnAction(actionEvent -> fillInputsWithTemplate());
            startTimeChoiceBox.setOnAction(actionEvent -> verifyInputTimeConflicts());
            endTimeChoiceBox.setOnAction(actionEvent -> verifyInputTimeConflicts());
            startTimeOfDay.setOnAction(actionEvent -> verifyInputTimeConflicts());
            endTimeOfDay.setOnAction(actionEvent -> verifyInputTimeConflicts());
            txtAreaNotes.setWrapText(true);
            txtAreaNotes.setId("text-area-regular");
        } catch (Exception e) {

        }
    }

    public void initEquipmentColumns() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("type"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("brand"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<AVEquipment, String>("notes"));

        tblEquipmentVIew.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

        updateEquipmentList();
    }

    private void updateEquipmentList() {
        tblEquipmentVIew.setItems(getEquipmentList());
    }

    private ObservableList<AVEquipment> getEquipmentList() {
        ObservableList<AVEquipment> equipmentObservableList = FXCollections.observableArrayList();
        ObservableList<AVEquipment> equipmentEntries = toReserveEquipment;

        for (AVEquipment ave :
                equipmentEntries) {
            equipmentObservableList.add(ave);
        }

        return equipmentObservableList;
    }


    private void initChoiceBoxes() {

        venueChoiceBox.getItems().addAll(venueContents);
        departmentChoiceBox.getItems().addAll(departmentContents);

        startTimeChoiceBox.getItems().addAll(timeContents);
        startTimeOfDay.getItems().addAll(" AM", " PM");
        endTimeChoiceBox.getItems().addAll(timeContents);
        endTimeOfDay.getItems().addAll(" AM", " PM");

        templateChoiceBox.setValue("Templates");
        templateChoiceBox.getItems().addAll(templateContents);

    }

    private void verifyPastDate() {
        LocalDate datePicked = datePicker.getValue();

        if (datePicked.isBefore(LocalDate.now())) {
            AVAlert.showMessage("Reservation Note", "You are selecting a past date.");
        }
    }

    private void verifyInputTimeConflicts() {
        String startTime = "";

        String endTime = "";
        if (allTimeInputsAreFilled()) {
            startTime = startTimeChoiceBox.getSelectionModel().getSelectedItem() +
                    startTimeOfDay.getSelectionModel().getSelectedItem();

            endTime = endTimeChoiceBox.getSelectionModel().getSelectedItem() +
                    endTimeOfDay.getSelectionModel().getSelectedItem();

            // check if start and end time are similar
            if ((startTime!=null && endTime!=null) && startTime.equals(endTime)) {
                AVAlert.showMessage("Time Conflict", "Start and End Time cannot be the same.\n Please try again.");
                endTimeChoiceBox.getSelectionModel().clearSelection();
                endTimeOfDay.getSelectionModel().clearSelection();
            }

            // check if start time is after end time
            if ((startTime!=null && endTime!=null) && (startTime.length() > 5 && endTime.length() > 5)) {

                AVTime avStart = new AVTime(startTime);
                AVTime avEnd = new AVTime(endTime);

                LocalTime ltStart = LocalTime.of(avStart.getTwentyFourHour(), Integer.valueOf(avStart.getMinutes()));
                LocalTime ltEnd = LocalTime.of(avEnd.getTwentyFourHour(), Integer.valueOf(avEnd.getMinutes()));

                if (ltStart.isAfter(ltEnd)) {
                    AVAlert.showMessage("Invalid Time", "Start time must precede end time.\n Please try again.");
                    endTimeChoiceBox.getSelectionModel().clearSelection();
                    endTimeOfDay.getSelectionModel().clearSelection();
                }

            }
        }
    }

    private boolean allTimeInputsAreFilled() {
        if ( startTimeChoiceBox.getSelectionModel().getSelectedItem() != null &&
                startTimeOfDay.getSelectionModel().getSelectedItem() != null &&
                endTimeChoiceBox.getSelectionModel().getSelectedItem() != null &&
                endTimeOfDay.getSelectionModel().getSelectedItem() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void initDatePickerFormat() {
//        datePicker.getEditor().setAlignment(Pos.CENTER);
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });

    }

    public void handleButton(ActionEvent actionEvent) throws IOException {

        if (actionEvent.getSource() == btnBack) {
            goBack();
        } else if (actionEvent.getSource() == btnExit) {
            handleExit();
        } else if (actionEvent.getSource() == btnClear) {
            verifyClearInputs();
        } else if (actionEvent.getSource() == btnVerifyEvent) {
            verifyEvent();
        } else if (actionEvent.getSource() == btnReserve) {
            handleReservation();
        } else if (actionEvent.getSource() == btnViewEquipment) {
            handleViewEquipment();
        }
    }

    private void handleViewEquipment() {
        try {
            if (toReserveEquipment == null || toReserveEquipment.size() == 0) {
                System.out.println("\t\tNo preselected items...");
            } else {
                System.out.println("\t\tPreselected items detected...");
                ReserveEquipment.setPreSelectedEquipment(tblEquipmentVIew.getItems(), toReserveEquipment);
            }
            toReserveEquipment = ReserveEquipment.getReservation();
            initEquipmentColumns();
        } catch (Exception e) {
        }
    }

    private void fillInputsWithTemplate() {

        try {
            String selectedTemplate = templateChoiceBox.getSelectionModel().getSelectedItem();

            if (selectedTemplate.equals("Templates")) {
                throw new NullPointerException();
            }

            if (inputsAreEmpty()) {
                fillInputs(selectedTemplate);
            } else if (isClickConfirmed("Fill Inputs with selected template?")) {
                fillInputs(selectedTemplate);
            }

        } catch (NullPointerException e) {
            AVAlert.showMessage("Select.", "Please select a template.");
        } catch (Exception e) {
            AVAlert.showMessage("Something went wrong.", "Please try again.");
            e.printStackTrace();
        }
    }

    private boolean inputsAreEmpty() {
        return txtActivity.getText().isEmpty() &&
                txtApprovedBy.getText().isEmpty() &&
                txtAreaNotes.getText().isEmpty() &&
                txtContactNumber.getText().isEmpty() &&
                txtContactPerson.getText().isEmpty() &&
                datePicker.getEditor().getText().isEmpty() &&
                startTimeChoiceBox.getSelectionModel().isSelected(-1) &&
                startTimeOfDay.getSelectionModel().isSelected(-1) &&
                endTimeChoiceBox.getSelectionModel().isSelected(-1) &&
                endTimeOfDay.getSelectionModel().isSelected(-1) &&
                departmentChoiceBox.getSelectionModel().isSelected(-1) &&
                venueChoiceBox.getSelectionModel().isSelected(-1);
    }



    private void fillInputs(String selectedTemplate) {

        String first = "Weekly Mass";
        String second = "Board of Directors Meeting";
        String third = "Seminar Documentation";
        String fourth = "Student Request";

        if (selectedTemplate.equals(first)) {
            txtActivity.setText("Weekly Mass");
            txtApprovedBy.setText("DEPARTMENT HEAD");
            txtAreaNotes.setText("Please verify if there's any special needs from the facilitator.");
            txtContactNumber.setText("Local 140");
            txtContactPerson.setText("Ma'am Rizalyn");
            datePicker.setValue(LocalDate.now());
            int EIGHT_AM = 15;
            startTimeChoiceBox.getSelectionModel().select(EIGHT_AM);
            startTimeOfDay.getSelectionModel().selectFirst();
            int NINE_AM = 16;
            endTimeChoiceBox.getSelectionModel().select(NINE_AM);
            endTimeOfDay.getSelectionModel().selectFirst();
            departmentChoiceBox.getSelectionModel().selectFirst();
            venueChoiceBox.getSelectionModel().select(1);
        } else if (selectedTemplate.equals(second)) {
            txtActivity.setText("Board of Directors Meeting");
            txtApprovedBy.setText("President's Office");
            txtAreaNotes.setText("Notify Sir Melvs, Sir Jovanny, Sir Dodong, and others");
            txtContactNumber.setText("Local 100");
            txtContactPerson.setText("Office Front Desk");
            datePicker.setValue(LocalDate.now());
            int EIGHT_AM = 14;
            startTimeChoiceBox.getSelectionModel().select(EIGHT_AM);
            startTimeOfDay.getSelectionModel().selectFirst();
            int NINE_AM = 15;
            endTimeChoiceBox.getSelectionModel().select(NINE_AM);
            endTimeOfDay.getSelectionModel().selectFirst();
            departmentChoiceBox.getSelectionModel().select(2);
            venueChoiceBox.getSelectionModel().select(3);
        } else if (selectedTemplate.equals(third)) {
            txtActivity.setText("Seminar Documentation");
            txtApprovedBy.setText("Executive Office");
            txtAreaNotes.setText("Please make sure to capture key events.");
            txtContactPerson.setText("Office Front Desk");
            txtContactNumber.setText("Local 100");
            datePicker.setValue(LocalDate.now());
            int EIGHT_AM = 13;
            startTimeChoiceBox.getSelectionModel().select(EIGHT_AM);
            startTimeOfDay.getSelectionModel().selectFirst();
            int NINE_AM = 14;
            endTimeChoiceBox.getSelectionModel().select(NINE_AM);
            endTimeOfDay.getSelectionModel().selectFirst();
            departmentChoiceBox.getSelectionModel().select(1);
            venueChoiceBox.getSelectionModel().select(2);
        } else if (selectedTemplate.equals(fourth)) {
            txtActivity.setText("Student Request");
            txtApprovedBy.setText("Staff In-charge");
            txtAreaNotes.setText("None.");
            txtContactNumber.setText("Local 143");
            txtContactPerson.setText("MSDO Staff In-charge");
            datePicker.setValue(LocalDate.now());
            int INDEX_FOUR = 4;
            startTimeChoiceBox.getSelectionModel().select(INDEX_FOUR);
            startTimeOfDay.getSelectionModel().selectFirst();
            int INDEX_FIVE = 5;
            endTimeChoiceBox.getSelectionModel().select(INDEX_FIVE);
            endTimeOfDay.getSelectionModel().selectFirst();
            departmentChoiceBox.getSelectionModel().selectLast();
            venueChoiceBox.getSelectionModel().selectLast();
        }
    }

    public void verifyClearInputs() {
        if (inputsAreEmpty() || isClickConfirmed("Do you want to clear all inputs?")) {
            clearInputs();
        }
    }

    private void clearInputs() {
        txtActivity.clear();
        txtApprovedBy.clear();
        txtAreaNotes.clear();
        txtContactNumber.clear();
        txtContactPerson.clear();
        datePicker.getEditor().clear();

        endTimeChoiceBox.getSelectionModel().clearSelection();
        endTimeOfDay.getSelectionModel().clearSelection();
        departmentChoiceBox.getSelectionModel().clearSelection();
        venueChoiceBox.getSelectionModel().clearSelection();

        tblEquipmentVIew.getSelectionModel().clearSelection();
        tblEquipmentVIew.getItems().clear();
    }


    private boolean isClickConfirmed(String message) {
        return AVAlert.confirmAction("Confirmation", message);
    }

    private void handleReservation() {
        boolean ok = AVAlert.confirmAction("Confirm reservation", "Press Yes to confirm.");

        if (ok) {
            try {
                parseReservation();
                if (reservation == null || equipmentBundle.size() == 0) {
                    throw new NullPointerException("Reservation invalid.");
                } else {
                    verifyReservation();
                    addReservationToManager();
                    makeEquipmentUnavailable(equipmentBundle);
                    promptAnotherReservation();
                    updateRecentActivity(CURRENT_ACCOUNT + " added a reservation.");
                    AVAlert.showMessage("Success", reservation.getEvent().getEventName() + "\n" +
                            reservation.getEvent().getEventDate() + "\n\n" +
                            reservation.getEvent().getEventStartTime() + " – " +
                            reservation.getEvent().getEventEndTime() + "\nEncoded by: " +
                            reservation.getEvent().getEncodedBy());
                }
            }  catch (IllegalArgumentException iae) {
                AVAlert.showMessage("Reservation Invalid.", "Reservation conflicts with another entry.\nPlease try again.");
            }
            catch (NullPointerException ne) {
                AVAlert.showMessage("Invalid Reservation", "Please make sure that \n" +
                        "all information are correct and try again.");
            } catch (Exception e) {
                //none
            }
        }
    }

    private void updateRecentActivity(String activity) {
        AVEMInfo.updateRecentActivity(activity);
        txtRecentActivity.setText("");
        txtRecentActivity.appendText(getRecentActivity());
    }

    private void verifyReservation() {
        //TODO implement

        verifyExistingReservation();
        verifyAllInputsAreFilled();
        verifyReservationTimeConflicts();
    }

    // check if reservation time has overlapping reservation in the same VENUE
    private void verifyReservationTimeConflicts() {

        String resVenue = reservation.getEvent().getEventVenue();

        ArrayList<AVReservation> similarVenue = new ArrayList();

        HashMap<String, AVReservation> resCollection = reservationManager.getReservationsFor(reservation.getEvent().getEventDate());

        // get all reservations with similar date and venue
        for (AVReservation r :
                resCollection.values()) {
            if (r.getEvent().getEventVenue().equals(resVenue)) {
                similarVenue.add(r);
            }
        }

        //check for overlapping time periods

        for (AVReservation r :
                similarVenue) {
            if (hasOverlap(r, reservation)) {
               throw new IllegalArgumentException("Reservation Conflict.");
            }
        }
    }

    private boolean hasOverlap(AVReservation collectionReservation, AVReservation localReservation) {
        AVTime avStart = localReservation.getEvent().getEventStartTime();
        AVTime avEnd = localReservation.getEvent().getEventEndTime();

        LocalTime lStart = LocalTime.of(avStart.getTwentyFourHour(), Integer.valueOf(avStart.getMinutes()));
        LocalTime lEnd = LocalTime.of(avEnd.getTwentyFourHour(), Integer.valueOf(avEnd.getMinutes()));


        avStart = collectionReservation.getEvent().getEventStartTime();
        avEnd = collectionReservation.getEvent().getEventEndTime();

        LocalTime cStart = LocalTime.of(avStart.getTwentyFourHour(), Integer.valueOf(avStart.getMinutes()));
        LocalTime cEnd = LocalTime.of(avEnd.getTwentyFourHour(), Integer.valueOf(avEnd.getMinutes()));

        if (cStart.isAfter(lStart) && cStart.isBefore(lEnd) ||
        (cEnd.isAfter(lStart) && cEnd.isBefore(lEnd)) ||
         (lStart.isAfter(cStart) && lStart.isBefore(cEnd))  ||
        (lEnd.isAfter(cStart) && lEnd.isBefore(cEnd))) {
            return true;
        } else {
            return false;
        }
    }

    private void verifyAllInputsAreFilled() {
        if (txtActivity.getText().isEmpty() ||
                txtApprovedBy.getText().isEmpty() ||
                txtAreaNotes.getText().isEmpty() ||
                txtContactNumber.getText().isEmpty() ||
                txtContactPerson.getText().isEmpty() ||
                datePicker.getEditor().getText().isEmpty() ||
                startTimeChoiceBox.getSelectionModel().isSelected(-1) ||
                startTimeOfDay.getSelectionModel().isSelected(-1) ||
                endTimeChoiceBox.getSelectionModel().isSelected(-1) ||
                endTimeOfDay.getSelectionModel().isSelected(-1) ||
                departmentChoiceBox.getSelectionModel().isSelected(-1) ||
                venueChoiceBox.getSelectionModel().isSelected(-1)) {
            System.out.println("An input is empty.");
            throw new NullPointerException("Please fill all the reservation details.");
        }
    }

    private void verifyExistingReservation() {
        AVReservation parsedRes = reservationManager.getReservation(reservation);
        if (parsedRes != null) {
            System.out.println("Conflict.");
            throw new IllegalArgumentException("Reservation Conflict");
        }
    }

    private void makeEquipmentUnavailable(HashMap<String, AVEquipment> equipmentBundle) {
        for (AVEquipment ave :
                equipmentBundle.values()) {
            equipmentManager.setEquipmentUnavailable(ave);
        }
    }

    private void promptAnotherReservation() throws IOException {
        boolean isYes = AVAlert.confirmAction("Reservation", "Do you want to have another reservation?");

        if (isYes) {
            clearInputs();
        } else {
            goBack();
        }
    }

    private void addReservationToManager() {
        reservation.setEquipmentStatus(AVReservation.RESERVED);
        reservationManager.addReservation(reservation);
    }

    private void parseReservation() {
        try {

            LocalDate ld = datePicker.getValue();
            AVDate avDate = new AVDate(String.valueOf(ld.getMonthValue()), String.valueOf(ld.getDayOfMonth()), String.valueOf(ld.getYear()));

            String appBy = txtApprovedBy.getText();
            String contNum = txtContactNumber.getText();
            String contPerson = txtContactPerson.getText();

            String start = startTimeChoiceBox.getSelectionModel().getSelectedItem()
                    + startTimeOfDay.getSelectionModel().getSelectedItem();
            String end = endTimeChoiceBox.getSelectionModel().getSelectedItem()
                    + endTimeOfDay.getSelectionModel().getSelectedItem();
            String department = departmentChoiceBox.getSelectionModel().getSelectedItem();
            String venue = venueChoiceBox.getSelectionModel().getSelectedItem();
            String notes = txtAreaNotes.getText();

            String encodedBy = AVEMInfo.CURRENT_ACCOUNT.getName();

            event = new AVEvent(txtActivity.getText(), avDate,
                    new AVTime(start), new AVTime(end), venue, department, contPerson,
                    contNum, appBy, notes, encodedBy, AVEvent.RESERVED_STATUS);

            equipmentBundle = new HashMap<>();
            ObservableList<AVEquipment> eq = tblEquipmentVIew.getItems();

            for (AVEquipment equipment :
                    eq) {
                equipmentBundle.put(equipment.getEquipmentID(), equipment);
            }

            reservation = new AVReservation(event, equipmentBundle);

        } catch (Exception e) {

        }
    }

    public void verifyEvent() {
        //TODO check if date has conflicts in the Calendar/collection

    }

    private void initBulletinPane() {
        AVEMInfo.initialize();
        txtAnnouncement.setWrapText(true);
        txtRecentActivity.setWrapText(true);
        txtRecentActivity.setEditable(false);
        txtAnnouncement.setEditable(false);
        txtRecentActivity.appendText(RECENT_ACTIVITY);
        txtAnnouncement.appendText(ANNOUNCEMENT);
    }

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
}
