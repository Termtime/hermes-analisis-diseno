package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.HashMap;
import java.util.Map;

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
import javafx.scene.control.ComboBox;
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
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.List;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.objects.Categoria;
import javax.swing.JOptionPane;

public class MantProductosModalAgregarProducto implements Initializable {
    @FXML private javafx.scene.control.Button btnCancelar;
    @FXML ComboBox<String> comboCategoria= new ComboBox<>();
    @FXML TextField txtNombreProducto;
    @FXML TextField  txtUnidad;
    FirebaseConnector db=FirebaseConnector.getInstance();
    List<QueryDocumentSnapshot> categoriaDocumentos = db.getAllDocumentsFrom(FirestoreRoutes.CATEGORIAS);

    @FXML private void btnCancelarClick(ActionEvent event) {

       Stage stage = (Stage) btnCancelar.getScene().getWindow();

       stage.close();

    }
    @FXML private void txtUnidadInput(ActionEvent event) {
        
    }
    @FXML private void comboCategoriaClick(ActionEvent event) {
        
    }
    @FXML private void txtNombreProductoInput(ActionEvent event) {
        
    }
    @FXML private void btnAgregarClick(ActionEvent event) {
        Map<String, Object> data= new HashMap<>();
        data.put("Producto", txtNombreProducto.getText());
        data.put("Unidad", txtUnidad.getText());
        data.put("Categoria", comboCategoria.getSelectionModel().getSelectedItem().toString());
        db.createDocument("Productos", data);
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
       stage.close();
    }
    @FXML AnchorPane mantProductosModalAgregarProducto;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        EventListeners.onWindowOpening(mantProductosModalAgregarProducto, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
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
