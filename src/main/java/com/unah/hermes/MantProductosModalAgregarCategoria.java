package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.swing.JOptionPane;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.stage.Modality;

public class MantProductosModalAgregarCategoria implements Initializable{
    @FXML AnchorPane AgregarCategoria;
    @FXML private javafx.scene.control.Button btnCancelar;
    @FXML TextField txtAgregarCategoria;
    @FXML private void btnCancelarClick(ActionEvent event) {

       Stage stage = (Stage) btnCancelar.getScene().getWindow();

       stage.close();

    }
    @FXML private void txtAgregarCategoriaInput(ActionEvent event) {
        
    }
    @FXML private void btnAgregarClick(ActionEvent event) {
        if(txtAgregarCategoria.getText().equals(""))
            {
                Navigation.pushRoute("AlertError", event, false, true);
                return;
            }else{
                Map<String, Object> data= new HashMap<>();
                data.put("nombre", txtAgregarCategoria.getText());
                
                try {
                    db.createDocument("Categorias", data);
                    Navigation.pushRoute("AlertExito", event, false, true);
                } catch (Exception e) {
                    Navigation.pushRoute("AlertError", event, false, true);
                }
                Stage stage = (Stage) btnCancelar.getScene().getWindow();

            stage.close();
            }   
    }
    FirebaseConnector db;
    @FXML AnchorPane mantProductosModalAgregarCategoria;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        EventListeners.onWindowOpening(mantProductosModalAgregarCategoria, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
                return null;
            }
            
        });
        db=FirebaseConnector.getInstance();
    }
}