package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.objects.RequisicionEntregadaRow;
import com.unah.hermes.objects.MantUsuarios;
import com.unah.hermes.provider.FirebaseConnector;
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

public class MantUsuariosPageController implements Initializable {

    @FXML
    ListView<MantUsuarios> listaMU;
    @FXML
    TableView<MantUsuarios> tablaUsuarios;

    @FXML
    private void btnAgregarUsuarioClick(ActionEvent event) {
        Navigation.pushRoute("agregarUsuario", event, false, true);
    }

    @FXML
    private void btnModificarUsuarioClick(ActionEvent event) {
        Navigation.pushRoute("modificarUsuario", event, false, true);
    }

    // @FXML public AnchorPane MantUsuario;

    // ListenerRegistration requisicionesListener;
    // FirebaseConnector db; corregir errores
    // MantUsuarios tablaUsuariosSelectedItem;
    // public static ObservableList<MantUsuarios> usuariosPermisos =
    // FXCollections.observableArrayList();

    // db = FirebaseConnector.getInstance();
    // requisicionesListener = db.iniciarListenerRequisiciones();
    // listaMU.setItems(usuariosPermisos);

    // //agregar el listener de cambio para cuando se cambie la seleccion
    // listaMU.getSelectionModel().selectedItemProperty().addListener(new
    // ChangeListener<Requisicion>(){
    // @Override
    // public void changed(ObservableValue<? extends Requisicion> observable,
    // Requisicion oldValue, Requisicion newValue) {
    // tablaUsuariosSelectedItem = newValue;
    // System.out.println(newValue);
    // popularTablaRequisicionesPDConProductos(tablaP, newValue.productos);
    // lblReqIDP.setText(newValue.reqID);
    // lblEstadoP.setText(newValue.estado);
    // lblFechaP.setText(newValue.fechaString);
    // lblHoraP.setText(newValue.hora);
    // lblAreaP.setText(newValue.area);
    // lblSolicitanteP.setText(newValue.solicitante);
    // }
    // });

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //
    }

}
