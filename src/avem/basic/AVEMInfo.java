package avem.basic;

import avem.core.AVAccount;
import avem.controllers.AVAlert;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AVEMInfo {

    public static AVAccount CURRENT_ACCOUNT = new AVAccount("guest", "guest", 3);
    public static Integer CURRENT_ACCOUNT_ACCESS = CURRENT_ACCOUNT.getAccess();

    public static HashMap<String, AVAccount> avAccountEntries;
    public static String RECENT_ACTIVITY = "";
    public static String ANNOUNCEMENT = "";
    public static String FREQUENTLY_ASKED_QUESTIONS = "" +
            "FAQ: I, dispense, wisdom. \n\n" +
            "Q: Oh great FAQ-sama, what's your wisdom? \n" +
            "A: No man is an island. Believe in yourself. " +
            "\n\tAsk others. Ask for help. Ask now.";

    private static File infoDIR;
    private static File announcementFile;
    private static File activityFile;
    private static File accountFile;

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
    public static boolean isFirstAccess = true;

    public AVEMInfo() {
        initialize();
    }

    public static void initialize() {
        if (isFirstAccess) {
            initDirectoryWithPath("avem-info");
            createAll();
            initAccountEntries();
            parseAllFileContents();
            appendSignInInfo();
            setFirstAccessAsFalse();
        }
    }

    public static void setFirstAccessAsFalse() {
        isFirstAccess = false;
    }

    public static void setFirstAccessAsTrue() {
        isFirstAccess = true;
    }

    private static void appendSignInInfo() {
        updateRecentActivity(CURRENT_ACCOUNT.getName() + " signed in.");

    }

    private static void parseAllFileContents() {
        ANNOUNCEMENT = getAnnouncementFileContents();
        RECENT_ACTIVITY = getActivityFileContents();
        parseAccountsFromFile();
    }

    private static void createAll() {
        createDirectory();
        createAnnouncementFile();
        createActivityFile();
        createAccountFile();
    }

    private static boolean directoryExists() {
        return infoDIR.exists();
    }

    private static boolean createDirectory() {
        if (directoryExists()) {
            return false;
        } else {
            infoDIR.mkdir();
            return true;
        }
    }

    private static void initDirectoryWithPath(String dirName) {
        infoDIR = new File(dirName);
    }


    //*** ACCOUNTS ***//
    private static void initAccountEntries() {
        avAccountEntries = new HashMap<String, AVAccount>();
        parseAccountsFromFile();
    }

    private static boolean createAccountFile() {
        accountFile = new File(infoDIR, "account-info.data");
        try {
            if (accountFile.exists()) {
            } else {
                accountFile.createNewFile();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // parse course from file and put to account hashmap here
    private static void parseAccountsFromFile() {

        try {
            String contents;
            String toParse;
            contents = getAccountFileContents();
            StringTokenizer st = new StringTokenizer(contents);
            while (st.hasMoreTokens()) {
                toParse = st.nextToken("[]");
                if (toParse.length() > 1) {
                    AVAccount avAccount = parseAccountFrom(toParse.trim());
                    avAccountEntries.put(avAccount.getUsername(), avAccount);
                }
            }
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AVAccount parseAccountFrom(String toParse) {
        StringTokenizer st = new StringTokenizer(toParse, "\n");

        //String[] info = new String[3];
        ArrayList<String> info = new ArrayList<>();

        int counter = 0;
        while (st.hasMoreTokens()) {
            info.add(st.nextToken());
            counter++;
        }

        int count = 0;
        AVAccount account = new AVAccount(info.get(count++), info.get(count++), Integer.valueOf(info.get(count++)), info.get(count++), info.get(count));
        return account;
    }

    public static String getAccountFileContents() {
        String contents = "";

        try {
            FileReader fr = new FileReader(accountFile);
            int currentCharacter = 0;
            while ((currentCharacter = fr.read()) != -1) {
                contents += (char) currentCharacter;
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    public static void updateAccountInfo(AVAccount accountSignedIn) {
        CURRENT_ACCOUNT = accountSignedIn;
        CURRENT_ACCOUNT_ACCESS = accountSignedIn.getAccess();
    }

    public static void resetAccounts() {
        try {
            initialize();
            accountFile.delete();
            createAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AVAccount getAccountInfo(String username, String password) {
        if (username.equals("admin") && password.equals("admin")) {
            return new AVAccount("admin", "admin", AVAccount.ADMIN, "Admin");
        } else if (username.equals("staff") && password.equals("staff")) {
            return new AVAccount("staff", "staff", AVAccount.INTERNAL_STAFF, "Staff");
        }

        AVAccount toValidate = new AVAccount(username, password);
        AVAccount parsedAccount = avAccountEntries.get(toValidate.getUsername());

        if (parsedAccount.equals(toValidate)) {
            return parsedAccount;
        } else {
            return null;
        }
    }

    public static boolean isValidAccount(String username, String password) {

        if (username.equals("admin") && password.equals("admin")) {
            CURRENT_ACCOUNT_ACCESS = AVAccount.ADMIN;
            CURRENT_ACCOUNT = new AVAccount("admin", "admin", AVAccount.ADMIN);
            return true;
        }

        AVAccount toValidate = new AVAccount(username, password);
        AVAccount parsedAccount = avAccountEntries.get(toValidate.getUsername());

        if (parsedAccount.equals(toValidate)) {
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String, AVAccount> getHashMap() {
        return avAccountEntries;
    }

    /***
     *
     * @param toGet
     */
    public AVAccount getAccount(AVAccount toGet) {
        if (toGet == null) {
            throw new IllegalArgumentException("Invalid argument, course must not be null");
        }
        return avAccountEntries.get(toGet.getUsername());
    }

    /***
     *
     * @param toAdd
     */
    public static void addAccount(AVAccount toAdd) {
        avAccountEntries.put(toAdd.getUsername(), toAdd);
        updateAccountsFile();
    }

    public static void updateAccountsFile() {
        try {
            String contents = getFormattedEquipment();
            FileWriter fw = new FileWriter(accountFile);
            fw.append(contents);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param toRemove
     */
    public static void removeAccount(AVAccount toRemove) {
        avAccountEntries.remove(toRemove.getUsername(), toRemove);
        updateAccountsFile();
    }

    /***
     *
     * @param toEdit
     */
    public static void editAccount(AVAccount toEdit, AVAccount withThisAVEquipment) {
        AVAccount parsed = avAccountEntries.get(toEdit.getUsername());

        if (parsed.equals(toEdit)) {
            avAccountEntries.remove(parsed.getUsername());
            avAccountEntries.put(withThisAVEquipment.getUsername(), withThisAVEquipment);
        }

        updateAccountsFile();
    }

    public static HashMap<String, AVAccount> getAllAccounts() {
        parseAccountsFromFile();

        return avAccountEntries;
    }

    public static String getFormattedEquipment() {
        String contents = "";
        try {
            for (AVAccount acc :
                    avAccountEntries.values()) {
                contents += "[" +
                        acc.getFormattedString()
                        + "]"
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }


    // ------------- ANNOUNCEMENT AND RECENT ACTIVITY METHODS --------------//

    public static void displayRecentActivity() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);


        Label lblHeader = new Label("Recent Activity");
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        TextArea txtRecentActivity = new TextArea();

        txtRecentActivity.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        txtRecentActivity.setWrapText(true);
        txtRecentActivity.setEditable(false);
        txtRecentActivity.appendText(RECENT_ACTIVITY);
        txtRecentActivity.setId("text-area-regular");

        Button btnOk = new Button("Ok");

        btnOk.setOnAction(actionEvent -> {
            window.close();
        });

        HBox mainHBox = new HBox(10);
        mainHBox.getChildren().addAll(btnOk);
        mainHBox.setAlignment(Pos.CENTER);

        VBox mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, txtRecentActivity, mainHBox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));


        Scene scene = new Scene(mainVbox);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);

        window.initStyle(StageStyle.TRANSPARENT);
        window.resizableProperty().setValue(false);
        window.setScene(scene);
        AVEMWindows.makeMovableScene(window, scene);
        window.showAndWait();
    }

    public static void displayAnnouncement() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Label lblHeader = new Label("Announcement");
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        TextArea txtAnnouncement = new TextArea();

        txtAnnouncement.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));
        txtAnnouncement.setWrapText(true);
        txtAnnouncement.setEditable(true);
        txtAnnouncement.appendText(ANNOUNCEMENT);
        txtAnnouncement.setId("text-area-regular");

        Button btnUpdateAnnouncement = new Button("Update Announcement");

        btnUpdateAnnouncement.setOnAction(actionEvent -> {
            String parseAnnouncement = txtAnnouncement.getText();

            if (!ANNOUNCEMENT.equals(parseAnnouncement)) {
                boolean confirmed = AVAlert.confirmAction("Update Announcement", "Update Announcement with Recent Changes?");

                if (confirmed) {
                    updateAnnouncement(parseAnnouncement);
                    AVEMInfo.updateRecentActivity(CURRENT_ACCOUNT + " updated the Announcement board.");
                    window.close();
                }
            } else {
                AVAlert.showMessage("Notice", "No changes has been made in the Announcement. \nPlease try again.");
            }

        });

        Button btnClose = new Button("Close");

        btnClose.setOnAction(actionEvent -> {

            String parseAnnouncement = txtAnnouncement.getText();

            if (!ANNOUNCEMENT.equals(parseAnnouncement)) {
                boolean confirmed = AVAlert.confirmAction("Confirm", "Changes in announcement file \nwill not be saved.\n\nContinue?");

                if (confirmed) {
                    window.close();
                }
            } else {
                window.close();
            }
        });

        HBox mainHBox = new HBox(10);
        mainHBox.getChildren().addAll(btnUpdateAnnouncement, btnClose);
        mainHBox.setAlignment(Pos.CENTER);

        VBox mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, txtAnnouncement, mainHBox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));

        Scene scene = new Scene(mainVbox);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);

        window.initStyle(StageStyle.TRANSPARENT);
        window.resizableProperty().setValue(false);
        window.setScene(scene);
        AVEMWindows.makeMovableScene(window, scene);
        window.showAndWait();
    }

    public static void displayFAQs() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Label lblHeader = new Label("Frequently Asked Questions");
        lblHeader.setFont(AVEMTheme.FONT_ALERT_HEADERS);
        TextArea txtFAQs = new TextArea();
        txtFAQs.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 13));
        txtFAQs.setWrapText(true);
        txtFAQs.setEditable(false);
        txtFAQs.appendText(FREQUENTLY_ASKED_QUESTIONS);

        Button btnOk = new Button("Thank you");

        btnOk.setOnAction(actionEvent -> {
            window.close();
        });

        HBox mainHBox = new HBox(10);
        mainHBox.getChildren().addAll(btnOk);
        mainHBox.setAlignment(Pos.CENTER);

        VBox mainVbox = new VBox(50);
        mainVbox.getChildren().addAll(lblHeader, txtFAQs, mainHBox);
        mainVbox.setAlignment(Pos.CENTER);
        mainVbox.setSpacing(50);
        mainVbox.setPadding(new Insets(50));

        Scene scene = new Scene(mainVbox);
        scene.getStylesheets().addAll(AVEMTheme.getStyleSheets());
        scene.setFill(Color.TRANSPARENT);

        window.initStyle(StageStyle.TRANSPARENT);
        window.resizableProperty().setValue(false);
        window.setScene(scene);
        AVEMWindows.makeMovableScene(window, scene);
        window.showAndWait();
    }

    private static boolean createAnnouncementFile() {
        announcementFile = new File(infoDIR, "announcement-info.data");
        try {
            if (announcementFile.exists()) {
            } else {
                announcementFile.createNewFile();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean createActivityFile() {
        activityFile = new File(infoDIR, "activity-info.data");
        try {
            if (activityFile.exists()) {
            } else {
                activityFile.createNewFile();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getAnnouncementFileContents() {
        String contents = "";

        try {
            FileReader fr = new FileReader(announcementFile);
            int currentCharacter = 0;
            while ((currentCharacter = fr.read()) != -1) {
                contents += (char) currentCharacter;
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static String getActivityFileContents() {
        String contents = "";

        try {
            FileReader fr = new FileReader(activityFile);
            int currentCharacter = 0;
            while ((currentCharacter = fr.read()) != -1) {
                contents += (char) currentCharacter;
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }

    private static void updateAnnouncementFile() {
        try {
            String contents = ANNOUNCEMENT;
            FileWriter fw = new FileWriter(announcementFile);
            fw.append(contents);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateActivityFile() {
        try {
            String contents = RECENT_ACTIVITY;
            FileWriter fw = new FileWriter(activityFile);
            fw.append(contents);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRecentActivity(String update) {
        RECENT_ACTIVITY = RECENT_ACTIVITY + "\n" + LocalDate.now().format(dateFormatter) + " | " +
                LocalTime.now().format(timeFormatter).toUpperCase(Locale.ROOT) + "  \n" + update + "\n";
        updateActivityFile();
    }

    public static void updateAnnouncement(String newAnnouncement) {
        ANNOUNCEMENT = newAnnouncement;
        updateAnnouncementFile();
    }

    public static void resetBulletin() {
        try {
            initialize();
            deleteBulletinFiles();
            createAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteBulletinFiles() {
        announcementFile.delete();
        activityFile.delete();
    }

    public static String getRecentActivity() {
        return RECENT_ACTIVITY;
    }

    public static String getAnnouncement() {
        return ANNOUNCEMENT;
    }
}
