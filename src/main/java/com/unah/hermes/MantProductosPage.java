package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

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
import com.unah.hermes.objects.Categoria;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.utils.Navigation;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Add;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;



public class MantProductosPage implements Initializable {
    @FXML TableView<Producto> tablaProductos;
    @FXML AnchorPane MantenimientoProductos;
    @FXML TextField txtFiltro;
    @FXML ComboBox<String> comboCategoria= new ComboBox<>();
    @FXML private void btnAgregarProductoClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosModalAgregarProducto", event, false, true);
        refreshProductos();
    }
    @FXML private void btnModificarProductoClick(ActionEvent event) {
        if(tablaProductoSelectedItem != null)
            Navigation.pushRouteWithParameter("MantProductosModalModificarProducto", event, false, true, MantProductosModalModificarProducto.class, tablaProductoSelectedItem );
        else{
            Alert alert = new Alert(AlertType.ERROR,"Debe seleccionar un Usuario antes", ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML private void btnEliminarProductoClick(ActionEvent event) {
        for (DocumentSnapshot doc : documentos) {
            Producto tmp;
            if(doc.exists()){
                tmp = new Producto(doc.getId(), doc.getString("Producto"), doc.getString("Unidad"), doc.getString("Categoria"));
                if(tmp.nombre!=null)
                {
                    if(tmp.nombre.equals(tablaProductoSelectedItem.getNombre().toString()))
                    {
                        db.deleteDocument("Productos", tmp.productoID);
                        refreshProductos();
                        break;
                    }
                }
            }
        }
        

    }
    
    @FXML private void btnAgregarCategoriaClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosModalAgregarCategoria", event, false, true);
    }
    @FXML private void btnModificarCategoriaClick(ActionEvent event) {
         Navigation.pushRoute("MantProductosModalModificarCategoria", event, false, true);
    }
    @FXML private void btnEliminarCategoriaClick(ActionEvent event) {
        for (DocumentSnapshot doc : categoriaDocumentos) {
            Categoria tmp;
            if(doc.exists()){
                tmp = new Categoria(doc.getId(),doc.getString("nombre"));
                if(tmp.Nombre!=null)
                {
                    if(tmp.Nombre.equals(comboCategoria.getSelectionModel().getSelectedItem().toString()))
                    {
                        db.deleteDocument("Categoria", tmp.CategoriaID);
                        refreshCategorias();
                        refreshProductos();
                        break;
                    }
                }
            }
        }
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
        tablaProductos.getItems().clear();
        List<Producto> categoriaFiltrados = new ArrayList<Producto>();
        for(Producto producto: productos){
            if(producto.categoria.toLowerCase().equals(comboCategoria.getSelectionModel().getSelectedItem().toString().toLowerCase())){
                categoriaFiltrados.add(producto);
            }    
        }
        tablaProductos.getItems().addAll(categoriaFiltrados);
    }

    FirebaseConnector db=FirebaseConnector.getInstance();
    Producto tablaProductoSelectedItem;
    
    ObservableList<Producto> productos = FXCollections.observableArrayList();
    List<QueryDocumentSnapshot> documentos = db.getAllDocumentsFrom(FirestoreRoutes.PRODUCTOS);
    List<QueryDocumentSnapshot> categoriaDocumentos = db.getAllDocumentsFrom(FirestoreRoutes.CATEGORIAS);
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
                for(DocumentSnapshot cat: categoriaDocumentos){
                    Categoria tmp;
                    if(cat.exists()){
                        tmp=new Categoria(cat.getId(),cat.getString("nombre"));
                        System.out.println(tmp.getNombre());
                        comboCategoria.getItems().add(tmp.getNombre());
                    }
                }
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
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Producto>(){
            @Override
            public void changed(ObservableValue<? extends Producto> observable, Producto oldValue, Producto newValue) {
                tablaProductoSelectedItem = newValue;
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
     private void refreshProductos(){
        tablaProductos.getItems().clear();
        productos.clear();
        documentos = db.getAllDocumentsFrom(FirestoreRoutes.PRODUCTOS);
        for (DocumentSnapshot doc : documentos) {
            Producto tmp;
            if(doc.exists()){
                tmp = new Producto(doc.getId(), doc.getString("Producto"), doc.getString("Unidad"), doc.getString("Categoria"));
                productos.add(tmp);
            }
        }
        tablaProductos.getItems().addAll(productos);
    }
    private void refreshCategorias(){
        comboCategoria.getItems().clear();
        categoriaDocumentos = db.getAllDocumentsFrom(FirestoreRoutes.CATEGORIAS);
        for(DocumentSnapshot cat: categoriaDocumentos){
            Categoria tmp;
            if(cat.exists()){
                tmp=new Categoria(cat.getId(),cat.getString("nombre"));
                System.out.println(tmp.getNombre());
                comboCategoria.getItems().add(tmp.getNombre());
            }
        }
    }
}