package com.unah.hermes;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.objects.Categoria;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MantProductosModalModificarCategoria implements Initializable{
    
    @FXML private void btnCancelarClick(ActionEvent event) {
        cerrarVentana();
    }
    @FXML private void btnModificarClick(ActionEvent event) {
        if(comboCategoria.getSelectionModel().isSelected(0)){
            Navigation.mostrarAlertError("Debe seleccionar una Categoria", event);
            return;
        }
        if(txtNuevoNombre.getText().equals("")){
            Navigation.pushRoute("AlertError", event, false, true);
            return;
        }
        Map<String, Object> data= new HashMap<>();
        data.put("nombre", txtNuevoNombre.getText());
        String categoriaid="";
        for(DocumentSnapshot cat2: categoriaDocumentos){
            Categoria tmp;
            if(cat2.exists()){
                tmp=new Categoria(cat2.getId(),cat2.getString("nombre"));
                System.out.println(tmp.Nombre+"categoria");
                if(tmp.Nombre!=null){
                    if(tmp.Nombre.equals(comboCategoria.getSelectionModel().getSelectedItem().toString()))
                    {
                        categoriaid=tmp.CategoriaID;
                    }
                }
            }
        }
        try {
            db.updateDocument("Categorias", categoriaid, data);
            Navigation.pushRoute("AlertExito", event, false, true);
            cerrarVentana();
        } catch (Exception e) {
            Navigation.mostrarAlertError("Falta llenar algunos campos en el formulario o debe seleccionar la categoria a modificar", event);
        }
        
    }
    
    @FXML ComboBox<String> comboCategoria;
    @FXML Button btnCancelar;
    @FXML TextField txtNuevoNombre;
    @FXML AnchorPane mantProductosModalModificarCategoria;
    FirebaseConnector db=FirebaseConnector.getInstance();
    List<QueryDocumentSnapshot> categoriaDocumentos = db.getAllDocumentsFrom(FirestoreRoutes.CATEGORIAS);

    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        
        EventListeners.onWindowOpening(mantProductosModalModificarCategoria, new Function<Window,Void>(){

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
                comboCategoria.getItems().add(tmp.getNombre());
            }
        }
    }

    private void cerrarVentana(){
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}