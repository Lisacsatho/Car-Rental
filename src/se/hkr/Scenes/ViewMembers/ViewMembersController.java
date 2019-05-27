package se.hkr.Scenes.ViewMembers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;
import se.hkr.Scenes.ReadController;
import se.hkr.UserSession;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ViewMembersController implements ReadController<Member>, Initializable {
    private ObservableList<Member> allMembers;
    private ObservableList<Member> matchingMembers;
    @FXML
    private TableView<Member> tblMembers;
    @FXML
    private TableColumn colSocialSecurityNo,
                        colName,
                        colDriversLicenseNo;
    @FXML
    private TextField txtFldSearch,
                      txtFldFirstName,
                      txtFldLastName,
                      txtFldPhoneNo,
                      txtFldDriversLicenseNo,
                      txtFldEmail,
                      txtFldStreet,
                      txtFldState,
                      txtFldZip;
    @FXML
    private Label lblSocialSecurityNo;

    @FXML
    private MenuItem
            menuItemHelp,
            menuItemLogOut,
            menuItemBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colSocialSecurityNo.setCellValueFactory(new PropertyValueFactory<Member, String>("socialSecurityNo"));
        colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Member) param.getValue()).getFullName());
            }
        });
        colDriversLicenseNo.setCellValueFactory(new PropertyValueFactory<Member, String>("driversLicenseNo"));

        updateMemberList();
        tblMembers.setItems(matchingMembers);

        tblMembers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            displayMember(newValue);
        });
    }

    private void updateMemberList() {
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
            allMembers = FXCollections.observableArrayList(memberDBHandler.readAll());
            if (matchingMembers == null) {
                matchingMembers = FXCollections.observableArrayList();
                matchingMembers.addAll(allMembers);
            } else {
                matchingMembers.clear();
                matchingMembers.addAll(allMembers);
            }
        } catch (Exception e) {
            Dialogue.alert("Could not connect to database." + e.getMessage());
        }
    }

    @FXML
    private void btnSavePressed() {
        if (tblMembers.getSelectionModel().getSelectedItem() != null) {
            if (Dialogue.alertOk("Are you sure?")) {
                updateMember(tblMembers.getSelectionModel().getSelectedItem());
                updateMemberList();
                resetDisplay();
                Dialogue.inform("Member was updated.");
            }
        } else {
            Dialogue.alert("Please choose a member to update.");
        }
    }

    private void updateMember(Member member) {
        if (!Pattern.matches("[1-9][1-9][1-9] [1-9][1-9]", txtFldZip.getText())) {
            Dialogue.alert("Zip code invalid, please use format XXX XX.");
        } else if (!Pattern.matches(".*[@].*[.].*", txtFldEmail.getText())) {
            Dialogue.alert("Email invalid, must include a @ and a dot.");
        } else {
            member.setFirstName(txtFldFirstName.getText());
            member.setLastName(txtFldLastName.getText());
            member.setPhoneNumber(txtFldPhoneNo.getText());
            member.setEmail(txtFldEmail.getText());
            member.setDriversLicenseNo(txtFldDriversLicenseNo.getText());
            member.getAddress().setState(txtFldState.getText());
            member.getAddress().setStreet(txtFldStreet.getText());
            member.getAddress().setZip(txtFldZip.getText());

            try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
                memberDBHandler.update(member);
            } catch (Exception e) {
                Dialogue.alert("Something went wrong when updating member in database." + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void displayMember(Member member) {
        try {
            lblSocialSecurityNo.setText(member.getSocialSecurityNo());
            txtFldFirstName.setText(member.getFirstName());
            txtFldLastName.setText(member.getLastName());
            txtFldEmail.setText(member.getEmail());
            txtFldPhoneNo.setText(member.getPhoneNumber());
            txtFldDriversLicenseNo.setText(member.getDriversLicenseNo());
            txtFldState.setText(member.getAddress().getState());
            txtFldStreet.setText(member.getAddress().getStreet());
            txtFldZip.setText(member.getAddress().getZip());
        } catch (NullPointerException e) {
            resetDisplay();
        }
    }

    private void resetDisplay() {
        lblSocialSecurityNo.setText("Social security no.");
        txtFldFirstName.clear();
        txtFldLastName.clear();
        txtFldEmail.clear();
        txtFldPhoneNo.clear();
        txtFldDriversLicenseNo.clear();
        txtFldState.clear();
        txtFldStreet.clear();
        txtFldZip.clear();
    }

    @FXML
    public void menuItemBackPressed(ActionEvent ae) { Navigator.getInstance().goBack(); }

    @FXML
    private void menuItemQuitPressed(ActionEvent ae) { System.exit(0);}

    @FXML
    private void buttonLogOutPressed() {
        UserSession.getInstance().resetSession();
        Navigator.getInstance().navigateToPanel();
    }

    @Override
    public boolean filter(Member model) {
        return false;
    }

    @Override
    public void search() {
        String key = txtFldSearch.getText().trim();
        matchingMembers.clear();
        if (key.equals("")) {
            matchingMembers.addAll(allMembers);
        } else {
            for (Member member : allMembers) {
                if (member.matches(key)) {
                    matchingMembers.add(member);
                }
            }
        }
    }
}