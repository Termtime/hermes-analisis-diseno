package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.utils.EventListeners;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.User; //Consulta objeto
import com.unah.hermes.utils.Navigation;

public class MantAreasPage implements Initializable {
        private FirebaseConnector db;

        private List<QueryDocumentSnapshot> documentos;
        private ObservableList<Area> areas;
        // Consulta
        @FXML
        TableView<Area> tablaArea;
        @FXML
        TableView<User> tablaUsuario;
        // Consulta
        @FXML
        AnchorPane MantenimientoAreas;

        @FXML
        private void btnCrearAreaClick(ActionEvent event) {
                Navigation.pushRoute("MantAreasModalCrearArea", event, false, true);
        }

        @FXML
        private void btnEliminarAreaClick(ActionEvent event) {

        }

        @FXML
        private void btnAgregarUsuarioAreaClick(ActionEvent event) {

        }

        @FXML
        private void btnEliminarUsuarioAreaClick(ActionEvent event) {

        }

        @Override
        public void initialize(URL url, ResourceBundle rb) {
                EventListeners.onWindowOpened(MantenimientoAreas, new Function<Window, Void>() {
                        @Override
                        public Void apply(Window parent) {
                                // iniciarEstructuraTablas();
                                return null;
                        }
                });

                MantenimientoAreas.widthProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                                        Number newValue) {
                                // recalcularColumnWidth();
                        }
                });

                db = FirebaseConnector.getInstance();
                documentos = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
                // for (QueryDocumentSnapshot doc : documentos) {
                // Area tmp;
                // if(doc.exists()){
                // tmp = new Area(doc.getId(), doc.getString("Area"), doc.get("areas"));
                // areas.add(tmp);
                // }
                // }
                // tablaAreas.getItems().addAll(areas);
        }
}
