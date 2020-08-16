package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.utils.Navigation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginPageController implements Initializable {

    private FirebaseConnector db;
    @FXML
    Label errorCorreo, errorPass;
    @FXML
    Button loginBtn;
    @FXML
    private TextField correoTxt;
    @FXML
    private PasswordField passTxt;

    private String correo, pass;

    @FXML
    private void loginBtnClick(ActionEvent event) {
        // resetear el estado de los labels de error
        errorCorreo.setVisible(false);
        errorPass.setVisible(false);
        errorCorreo.setText("");
        errorPass.setText("");
        // procesar el login
        correo = correoTxt.getText();
        pass = passTxt.getText();
        String response = db.loginWithEmailPassword(correo, pass);
        if (response.equals("PASS")) {
            Navigation.pushRoute("MainPageMenu", event, true, false);
        } else if (response.equals("ERROR")) {
            errorCorreo.setText("Error desconocido, contacte al personal de IT");
            errorCorreo.setVisible(true);
        } else if (response.equals("EMAIL_NOT_FOUND")) {
            errorCorreo.setText("Usuario no encontrado");
            errorCorreo.setVisible(true);
        } else if (response.equals("INVALID_PASSWORD")) {
            errorPass.setText("Credenciales invalidas");
            errorPass.setVisible(true);
        } else if (response.equals("USER_DISABLED")) {
            errorCorreo.setText("Usuario deshabilitado");
            errorCorreo.setVisible(true);
        } else if (response.equals("TOO_MANY_ATTEMPTS_TRY_LATER")) {
            errorPass.setText("Demasiados intentos, intente más tarde");
            errorPass.setVisible(true);
        } else if (response.equals("MISSING_PASSWORD")) {
            errorPass.setText("Credenciales invalidas");
            errorPass.setVisible(true);
        }
    }

    @FXML
    private void validarCorreo(KeyEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        correoTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorCorreo.setVisible(false);
                errorPass.setVisible(false);
                errorCorreo.setText("");
                errorPass.setText("");
                if (newValue.isEmpty()) {
                    loginBtn.setDisable(true);
                    errorCorreo.setVisible(false);
                    errorCorreo.setText("Debe ingresar un correo válido");
                    return;
                }
                String regexString = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern regex = Pattern.compile(regexString);
                Matcher matcher = regex.matcher(newValue);
                if (matcher.find()) {
                    loginBtn.setDisable(false);
                    errorCorreo.setVisible(false);
                } else {
                    errorCorreo.setVisible(true);
                    loginBtn.setDisable(true);
                    errorCorreo.setText("Debe ingresar un correo válido");
                }
            }
        });
        db = FirebaseConnector.getInstance();
        errorCorreo.setVisible(false);
        errorPass.setVisible(false);
    }

}