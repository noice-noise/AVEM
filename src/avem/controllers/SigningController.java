package avem.controllers;

import avem.basic.AVEMInfo;
import avem.basic.AVEMTheme;
import avem.basic.AVEMWindows;
import avem.core.AVAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static avem.basic.AVEMTheme.VERSION;

public class SigningController implements Initializable {


    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnAbout;
    @FXML
    private Button btnForgotPassword;
    @FXML
    private Button btnSignIn;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnSignInAsGuest;
    @FXML
    private Button btnQuit;

    private AVEMInfo accountInfo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AVEMInfo.setFirstAccessAsTrue();
        FillSuperAdminInfo();
    }

    private void FillSuperAdminInfo() {
        txtUsername.setText(("admin"));
        txtPassword.setText("admin");
    }

    @FXML
    public void handleButton(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnForgotPassword) {
            AVAlert.showMessage("Forgot Password", "The recovery link has been sent to the email.\n\nAlso, you can inform the assigned Department Head\nto reset your password instead.");
        } else if (actionEvent.getSource() == btnAbout) {
            AVAlert.showMessage("About", "Developed by Team Graduate Rata \n\n Comeros, RJr.\nSaycon,\tDR.\nSeblos,\tSJ.\nQuiros,\tPJ.");
        } else if (actionEvent.getSource() == btnQuit) {
            handleExit();
        }
    }

    public void handleExit() {
        boolean ok = AVAlert.confirmAction("Exiting AVEM", "Are you sure you want to quit?");
        if (ok) {
            Stage stage = (Stage) btnQuit.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleSignIn(ActionEvent actionEvent) throws IOException {
        try {
            if (actionEvent.getSource() == btnSignInAsGuest) {
                AVEMInfo.updateAccountInfo(new AVAccount("Guest", "guest", 3, "Guest"));
                switchToDashboard(actionEvent);
            } else if (actionEvent.getSource() == btnSignIn) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    AVAlert.showMessage("Enter information", "Please enter your account information.");
                } else {
                    System.out.println("User: " + txtUsername.getText());
                    System.out.println("Password: " + txtPassword.getText());

                    if (AVEMInfo.isValidAccount(username, password)) {
                        AVAccount accountSignedIn = AVEMInfo.getAccountInfo(username, password);
                        AVEMInfo.updateAccountInfo(accountSignedIn);
                        switchToDashboard(actionEvent);
                    } else {
                        throw new IllegalArgumentException("Account invalid.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ERR] Error in handling sign in.");
            txtPassword.clear();
            AVAlert.showMessage("Please try again.", "Username or password is incorrect.");
            e.printStackTrace();
        }
    }

    private void switchToDashboard(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
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
}