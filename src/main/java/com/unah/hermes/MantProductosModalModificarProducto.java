package com.unah.hermes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.objects.Categoria;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MantProductosModalModificarProducto implements Initializable {
    
    
    @FXML private void btnCancelarClick(ActionEvent event) {

       Stage stage = (Stage) btnCancelar.getScene().getWindow();

       stage.close();

    }
    @FXML private void btnModificarClick(ActionEvent event) {
        if(txtNombreProducto.getText().equals("")||txtUnidad.getText().equals(""))
        {
            Navigation.pushRoute("AlertError", event, false, true);
            return;
        }
        Map<String, Object> data= new HashMap<>();
        data.put("Producto", txtNombreProducto.getText());
        data.put("Unidad", txtUnidad.getText());
        data.put("Categoria", comboCategoria.getSelectionModel().getSelectedItem().toString());
        try {
            db.updateDocument("Productos", productoID, data);
            documentos = db.getAllDocumentsFrom(FirestoreRoutes.PRODUCTOS);
            productos.clear();
            for (DocumentSnapshot doc : documentos) {
                Producto tmp;
                if(doc.exists()){
                    tmp = new Producto(doc.getId(), doc.getString("Producto"), doc.getString("Unidad"), doc.getString("Categoria"));
                    productos.add(tmp);
                }
            }
                for(Producto producto: productos){
                    if(producto.nombre.toLowerCase().equals(txtNombreProducto.getText().toLowerCase())){
                        db.uploadImage(FirestoreRoutes.PRODUCTOS+"/Productos", selectedFile, producto.productoID);
                    }
                }
            Navigation.pushRoute("AlertExito", event, false, true);
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            Navigation.pushRoute("AlertError", event, false, true);
        }
        
    }
    @FXML private void btnAgregarImagenProductoClick(ActionEvent event){
        try {
            FileChooser fileChooser= new FileChooser();
            fileChooser.setTitle("Seleccione una Foto");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png","*.jpg"));
            selectedFile=fileChooser.showOpenDialog(ventanaPrincipal);
            if(selectedFile!= null){
                InputStream is=new FileInputStream(selectedFile);
                imagenProducto.setImage(new Image(is));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML TextField txtNombreProducto;
    @FXML TextField txtUnidad;
    @FXML ComboBox<String> comboCategoria= new  ComboBox<>();
    @FXML Button btnCancelar;
    @FXML ImageView imagenProducto;

    Producto productoData;
    String productoID;
    File selectedFile;
    Window ventanaPrincipal;
    List<QueryDocumentSnapshot> documentos;
    FirebaseConnector db=FirebaseConnector.getInstance();
    List<QueryDocumentSnapshot> categoriaDocumentos = db.getAllDocumentsFrom(FirestoreRoutes.CATEGORIAS);
    ObservableList<Producto> productos = FXCollections.observableArrayList();
    public void initData(Object data){
        productoData = (Producto) data;
        txtNombreProducto.setText(productoData.nombre);
        txtUnidad.setText(productoData.unidad);
        productoID=productoData.getProductoID();
        comboCategoria.getSelectionModel().select(productoData.categoria);
    }
    @FXML AnchorPane mantProductosModalModificarProducto;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        EventListeners.onWindowOpening(mantProductosModalModificarProducto, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
                ventanaPrincipal=t;
                return null;
            }
            
        });
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