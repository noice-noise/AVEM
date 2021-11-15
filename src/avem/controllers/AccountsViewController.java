package avem.controllers;

import avem.basic.AVEMInfo;

import avem.core.AVAccount;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

import java.util.HashMap;
import java.util.ResourceBundle;

import static avem.basic.AVEMInfo.CURRENT_ACCOUNT;


public class AccountsViewController implements Initializable {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnCancel;
    @FXML
    private ListView<AVAccount> lvAccounts;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initListView();
    }

    private void initListView() {

        HashMap<String, AVAccount> acc = AVEMInfo.getAllAccounts();
        for (AVAccount ac:
             acc.values()) {
            lvAccounts.getItems().add(ac);
        }
    }

    public void goBack() throws IOException {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void handleButton(javafx.event.ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnAdd) {
            AVAccount acc = AccountView.displayAdd();

            if (acc != null) {
                AVEMInfo.addAccount(acc);
                lvAccounts.getItems().add(acc);
                AVEMInfo.updateRecentActivity(CURRENT_ACCOUNT + " signed up a new account: " + acc.getName() + ".");
            }

        } else if (actionEvent.getSource() == btnEdit) {
            if (lvAccounts.getSelectionModel().getSelectedItem() == null) {
                AVAlert.showMessage("Select Account", "Please select an account first.");
            } else {
                AVAccount selectedAccount = lvAccounts.getSelectionModel().getSelectedItem();
                AVAccount editedAccount = AccountView.displayEdit(selectedAccount);

                if (editedAccount != null) {
                    AVEMInfo.editAccount(selectedAccount, editedAccount);
                    lvAccounts.getItems().remove(selectedAccount);
                    lvAccounts.getItems().add(editedAccount);
                    AVEMInfo.updateRecentActivity(CURRENT_ACCOUNT + " updated an account: " + selectedAccount.getName() + ".");
                }
            }
        } else if (actionEvent.getSource() == btnRemove) {
            if (lvAccounts.getSelectionModel().getSelectedItem() == null) {
                AVAlert.showMessage("Select Account", "Please select an account first.");
            } else {
                AVAccount selectedAccount = lvAccounts.getSelectionModel().getSelectedItem();
                boolean confirmed = AVAlert.confirmAction("Confirm",
                        "Are you sure you want to remove\nselected account?");

                if (selectedAccount != null && confirmed) {
                    AVEMInfo.removeAccount(selectedAccount);
                    lvAccounts.getItems().remove(selectedAccount);
                    AVEMInfo.updateRecentActivity(CURRENT_ACCOUNT + " removed an account: " + selectedAccount.getName() + ".");
                }
            }
        } else if (actionEvent.getSource() == btnCancel) {
            goBack();
        }
    }
}
