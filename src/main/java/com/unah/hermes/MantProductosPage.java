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
import com.unah.hermes.provider.FirebaseConnector;
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

import com.unah.hermes.objects.MantenimientoProducto;
import com.unah.hermes.utils.Navigation;




public class MantProductosPage implements Initializable {
    @FXML TableView<MantenimientoProducto> tablaProductos;
    @FXML AnchorPane MantenimientoProductos;
    @FXML private void btnAgregarProductoClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosModalAgregarProducto", event, false, true);
    }
    @FXML private void btnModificarProductoClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosModalModificarProducto", event, false, true);
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
    @FXML private void txtFiltroInput(ActionEvent event) {
        
    }
    @FXML private void comboCategoriaClick(ActionEvent event) {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EventListeners.onWindowOpened(MantenimientoProductos, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();
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
         columnaProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
         columnaProducto.setPrefWidth(tablaProductos.getWidth()*0.35);
        
         TableColumn columnaCategoria = new TableColumn<>("Categoria");
         columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
         columnaCategoria.setPrefWidth(tablaProductos.getWidth()*0.35);
         
         TableColumn columnaUnidad = new TableColumn<>("Unidad");
         columnaUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
         columnaUnidad.setPrefWidth(tablaProductos.getWidth()*0.30);
 
         tablaProductos.getColumns().addAll(columnaProducto, columnaCategoria, columnaUnidad);
     }
    //  private void popularTablaMantProductos(TableView<MantenimientoProducto> tabla, ObservableList<MantenimientoProducto> Mantenimiento) {
    //     try{
    //         tabla.getItems().clear();
    //         for (MantenimientoProducto mantenimiento : Mantenimiento) {
    //             MantenimientoProducto row = new MantenimientoProducto(mantenimiento.producto,mantenimiento.categoria,mantenimiento.unidad);
    //             tabla.getItems().add(row);
    //         }
    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }
    //}
}
