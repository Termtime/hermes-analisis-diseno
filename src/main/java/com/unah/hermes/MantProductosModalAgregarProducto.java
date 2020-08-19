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

public class MantProductosModalAgregarProducto implements Initializable {

    @FXML
    private void btnCancelarClick(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void btnAgregarClick(ActionEvent event) {

        Map<String, Object> data = new HashMap<>();
        // removido temporalmente, preguntar a la Ingeniera
        // if(selectedFile==null){
        // Navigation.mostrarAlertError("Debe seleccionar una Imagen", event);
        // return;
        // }
        if (comboCategoria.getSelectionModel().getSelectedIndex() == -1) {
            Navigation.mostrarAlertError("Debe seleccionar una categoría.", event);
            return;
        }
        if (txtNombreProducto.getText().equals("") || txtUnidad.getText().equals("")) {
            Navigation.mostrarAlertError("Falta llenar algunos campos en el formulario.", event);
            return;
        }
        data.put("Producto", txtNombreProducto.getText());
        data.put("Unidad", txtUnidad.getText());
        data.put("Categoria", comboCategoria.getSelectionModel().getSelectedItem().categoriaID);
        try {

            String productoID = db.createDocument("Productos", data);
            documentos = db.getAllDocumentsFrom(FirestoreRoutes.PRODUCTOS);
            productos.clear();
            if (selectedFile != null) {
                db.uploadImage(FirestoreRoutes.PRODUCTOS + "/Productos", selectedFile, productoID);
            }
            Navigation.mostrarAlertExito("Categoria agregada con éxito.", event);
            cerrarVentana();
        } catch (Exception e) {
            Navigation.mostrarAlertError("Falta llenar algunos campos en el formulario.", event);
        }

    }

    @FXML
    private void btnAgregarImagenProductoClick(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione una Foto");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
            selectedFile = fileChooser.showOpenDialog(ventanaPrincipal);
            if (selectedFile != null) {
                InputStream is = new FileInputStream(selectedFile);
                imagenProducto.setImage(new Image(is));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    Button btnCancelar;
    @FXML
    ComboBox<Categoria> comboCategoria;
    @FXML
    TextField txtNombreProducto;
    @FXML
    TextField txtUnidad;
    @FXML
    ImageView imagenProducto;
    @FXML
    AnchorPane mantProductosModalAgregarProducto;
    File selectedFile;
    Window ventanaPrincipal;
    List<QueryDocumentSnapshot> documentos;
    FirebaseConnector db = FirebaseConnector.getInstance();
    List<QueryDocumentSnapshot> categoriaDocumentos = db.getAllDocumentsFrom(FirestoreRoutes.CATEGORIAS);
    ObservableList<Producto> productos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EventListeners.onWindowOpening(mantProductosModalAgregarProducto, new Function<Window, Void>() {

            @Override
            public Void apply(Window t) {
                ((Stage) t).resizableProperty().setValue(Boolean.FALSE);
                ventanaPrincipal = t;
                return null;
            }

        });
        for (DocumentSnapshot cat : categoriaDocumentos) {
            Categoria tmp;
            if (cat.exists()) {
                tmp = new Categoria(cat.getId(), cat.getString("nombre"));
                comboCategoria.getItems().add(tmp);
            }
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
