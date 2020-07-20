package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.cloud.firestore.Firestore;
import com.unah.hermes.provider.FirebaseConnector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LoginPageController implements Initializable {
    
    private FirebaseConnector db;

    @FXML
    private TextField correoTxt;
    @FXML
    private PasswordField passTxt;
    private String correo, pass;
    @FXML
    private void loginBtnClick(ActionEvent event)
    {
        correo = correoTxt.getText();
        pass = passTxt.getText();

        db.loginWithEmailPassword(correo, pass);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = FirebaseConnector.getInstance();
        
    }    
    
}