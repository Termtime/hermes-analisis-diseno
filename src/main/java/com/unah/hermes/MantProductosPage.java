package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
//import sun.swing.SwingAccessor.JTextComponentAccessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.swing.JTextField;

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

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.utils.Navigation;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;



public class MantProductosPage implements Initializable {
    @FXML TableView<Producto> tablaProductos;
    @FXML AnchorPane MantenimientoProductos;
    @FXML TextField txtFiltro;
    @FXML private void btnAgregarProductoClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosModalAgregarProducto", event, false, true);
    }
    @FXML private void btnModificarProductoClick(ActionEvent event) {
        // if(TablaProductoSelectedItem != null)
        //     Navigation.pushRouteWithParameter("MantProductosModalModificarProducto", event, false, true, MantProductosModalModificarProducto.class,TablaProductoSelectedItem,MantProductosPage.class);
        // else{
        //      Alert alert = new Alert(AlertType.ERROR,"Debe seleccionar una producto antes", ButtonType.OK);
        //      alert.showAndWait();
        //}
        
        
    }
    @FXML private void btnEliminarProductoClick(ActionEvent event) {
        
    }
    @FXML private void btnAgregarCategoriaClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosModalAgregarCategoria", event, false, true);
    }
    @FXML private void btnModificarCategoriaClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosModalModificarCategoria", event, false, true);
    }
    @FXML private void btnEliminarCategoriaClick(ActionEvent event) {

    }
    @FXML private void txtFiltroInput(KeyEvent event) {
        tablaProductos.getItems().clear();
        List<Producto> productosFiltrados = new ArrayList<Producto>();
        for(Producto producto: productos){
            if(producto.nombre.toLowerCase().contains(txtFiltro.getText().toLowerCase()) || txtFiltro.getText().equals("")){
                productosFiltrados.add(producto);
            }    
        }
        tablaProductos.getItems().addAll(productosFiltrados);
    }
    @FXML private void comboCategoriaClick(ActionEvent event) {
   
    }

    FirebaseConnector db=FirebaseConnector.getInstance();
    Producto TablaProductoSelectedItem;
    
    ObservableList<Producto> productos = FXCollections.observableArrayList();
    List<QueryDocumentSnapshot> documentos = db.getAllDocumentsFrom(FirestoreRoutes.PRODUCTOS);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MantenimientoProductos.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                MantenimientoProductos.requestFocus();
            }
        });
        EventListeners.onWindowOpened(MantenimientoProductos, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();
                
                for (DocumentSnapshot doc : documentos) {
                    Producto tmp;
                    if(doc.exists()){
                        tmp = new Producto(doc.getId(), doc.getString("Producto"), doc.getString("Unidad"), doc.getString("Categoria"));
                        productos.add(tmp);
                    }
                }
                tablaProductos.getItems().addAll(productos);

                 return null;
            }
        });
        MantenimientoProductos.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });

        tablaProductos.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });
    }   

    private void recalcularColumnWidth(){
                       
        ObservableList columnasMantenimiento = tablaProductos.getColumns();

        ((TableColumn)( columnasMantenimiento.get(0) )).setPrefWidth(tablaProductos.getWidth()*0.35);
        ((TableColumn)( columnasMantenimiento.get(1) )).setPrefWidth(tablaProductos.getWidth()*0.35);
        ((TableColumn)( columnasMantenimiento.get(2) )).setPrefWidth(tablaProductos.getWidth()*0.30);
        
    }
    private void iniciarEstructuraTablas(){
        
         tablaProductos.getItems().clear();
         tablaProductos.getColumns().clear();
         TableColumn columnaProducto = new TableColumn<>("Producto");
         columnaProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
         columnaProducto.setPrefWidth(tablaProductos.getWidth()*0.38);
         columnaProducto.setResizable(false);

         TableColumn columnaCategoria = new TableColumn<>("Categoria");
         columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
         columnaCategoria.setPrefWidth(tablaProductos.getWidth()*0.30);
         columnaCategoria.setResizable(false);

         TableColumn columnaUnidad = new TableColumn<>("Unidad");
         columnaUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
         columnaUnidad.setPrefWidth(tablaProductos.getWidth()*0.30);
         columnaUnidad.setResizable(false);
         
         tablaProductos.getColumns().addAll(columnaProducto, columnaCategoria, columnaUnidad);
     }
}