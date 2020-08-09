package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.util.function.Function;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.objects.RequisicionEntregadaRow;
import com.unah.hermes.objects.RequisicionRow;
import com.unah.hermes.objects.User;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import org.apache.commons.lang3.ArrayUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.stage.Modality;

public class EntregaReqPage implements Initializable {
    @FXML private javafx.scene.control.Button btnCancelar;
    @FXML private javafx.scene.control.Button btnTerminarEntrega;
    @FXML ListView<Requisicion> listaRQE;
    @FXML TableView<RequisicionEntregadaRow> tablaE;

    ListenerRegistration requisicionesListener;
    FirebaseConnector db;
    Requisicion tablaESelectedItem = null;

    @FXML public void btnTerminarEntregaClick(ActionEvent event){
        if(tablaESelectedItem != null)
            // Update an existing document
                //DocumentReference docRef = db.collection("cities").document("DC");
                

               
        else{
            Alert alert = new Alert(AlertType.ERROR,"Debe seleccionar una requisicion pendiente antes", ButtonType.OK);
            alert.showAndWait();
        }    

    }

    @FXML private void btnCancelarClick(ActionEvent event) {

        Stage stage = (Stage) btnCancelar.getScene().getWindow();
 
        stage.close();
 
     }

    
    public void initData(Object data){
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
       
        





    }    
    
}