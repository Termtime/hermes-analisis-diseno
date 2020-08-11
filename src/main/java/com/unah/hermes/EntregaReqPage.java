package com.unah.hermes;
import javafx.beans.value.ChangeListener;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.scene.control.ListView;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EditableIntegerTableCell;
import com.unah.hermes.utils.EditableStringTableCell;
import com.unah.hermes.utils.EventListeners;

import org.apache.commons.lang3.ArrayUtils;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class EntregaReqPage implements Initializable {
    @FXML public javafx.scene.control.Button btnCancelar;
    @FXML public javafx.scene.control.Button btnTerminarEntrega;
    @FXML TableView<Producto> tablaVistaPrevia;
    @FXML ListView<Requisicion> listaRQP;
    @FXML Label labelID;
    @FXML Label labelHora;
    @FXML Label labelFecha;
    @FXML Label labelArea;
    @FXML Label labelSolicitante;
    @FXML AnchorPane entregaReqPage; 

    @FXML public void btnTerminarEntregaClick(ActionEvent event){
        ObservableList<Producto>productosEditados = tablaVistaPrevia.getItems();
        // for (Producto producto : productosEditados) {
        //     System.out.print(producto.nombre);
        //     System.out.print("-");
        //     System.out.print(producto.unidad);
        //     System.out.print("-");
        //     System.out.print(producto.cantPedida);
        //     System.out.print("-");
        //     System.out.print(producto.cantEntregada);
        //     System.out.print("-");
        //     System.out.print(producto.cantPendiente);
        //     System.out.print("-");
        //     System.out.println(producto.comentario);
        // }
        requisicionSeleccionada.productos = productosEditados;
        Map<String,Object> updateData = new HashMap<String,Object>();
        updateData.put("productos", requisicionSeleccionada.productos);
        updateData.put("estado", "Entregada");
        db.updateDocument(FirestoreRoutes.REQUISICIONES, requisicionSeleccionada.reqID, updateData);
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML public void btnCancelarClick(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML AnchorPane EntregaReqPage;

    ListenerRegistration requisicionesListener;
    FirebaseConnector db;
    Requisicion requisicionSeleccionada;
    List<Producto> productos;
    public void initData(Object obj){
        requisicionSeleccionada = (Requisicion) obj;
        // labelID.setText(requisicionSeleccionada.reqID);
        labelArea.setText(requisicionSeleccionada.area);
        labelFecha.setText(requisicionSeleccionada.fechaString);
        labelHora.setText(requisicionSeleccionada.hora);
        labelSolicitante.setText(requisicionSeleccionada.solicitante);
        productos = requisicionSeleccionada.productos;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        db = FirebaseConnector.getInstance();
        entregaReqPage.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                entregaReqPage.requestFocus();
            }
        });
        entregaReqPage.setPrefWidth(1080);
        EventListeners.onWindowOpened(entregaReqPage, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                
                iniciarEstructuraTabla();
                tablaVistaPrevia.getItems().addAll(productos);
                parent.widthProperty().addListener(new ChangeListener<Number>(){

                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        recalcularColumnWidth();
                    }
                    
                });

                return null;
            }
        });

    }

    private void recalcularColumnWidth(){
        ObservableList<TableColumn<Producto,?>> columnas = tablaVistaPrevia.getColumns();
        ((TableColumn<Producto,?>)( columnas.get(0) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.30);
        ((TableColumn<Producto,?>)( columnas.get(1) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.15);
        ((TableColumn<Producto,?>)( columnas.get(2) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        ((TableColumn<Producto,?>)( columnas.get(3) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        ((TableColumn<Producto,?>)( columnas.get(4) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        ((TableColumn<Producto,?>)( columnas.get(5) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.22);
    }   

    private void iniciarEstructuraTabla(){
        tablaVistaPrevia.setEditable(true);
        tablaVistaPrevia.getItems().clear();
        tablaVistaPrevia.getColumns().clear();
        TableColumn<Producto,String> columnaProducto = new TableColumn<>("Producto");
        columnaProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaProducto.setPrefWidth(tablaVistaPrevia.getWidth()*0.30);
        columnaProducto.setResizable(false);

        TableColumn<Producto,String> columnaUnidad = new TableColumn<>("Unidad");
        columnaUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        columnaUnidad.setPrefWidth(tablaVistaPrevia.getWidth()*0.15);
        columnaUnidad.setResizable(false);

        TableColumn<Producto,String> columnaCantidadPedida = new TableColumn<>("C. Pedida");
        columnaCantidadPedida.setCellValueFactory(new PropertyValueFactory<>("cantPedida"));
        columnaCantidadPedida.setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        columnaCantidadPedida.setResizable(false);

        TableColumn<Producto,Integer> columnaCantidadEntregada = new TableColumn<>("C. Entregada");
        columnaCantidadEntregada.setEditable(true);
        columnaCantidadEntregada.setCellValueFactory(new PropertyValueFactory<>("cantEntregada"));
        columnaCantidadEntregada.setCellFactory(col -> new EditableIntegerTableCell<Producto>());
        columnaCantidadEntregada.setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        columnaCantidadEntregada.setResizable(false);
        columnaCantidadEntregada.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Producto, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Producto, Integer> t) {
                t.getRowValue().cantEntregada = t.getNewValue();
                
            }
        });

        TableColumn<Producto,Integer> columnaCantidadPendiente = new TableColumn<>("C. Pendiente");
        columnaCantidadPendiente.setEditable(true);
        columnaCantidadPendiente.setCellValueFactory(new PropertyValueFactory<>("cantPendiente"));
        columnaCantidadPendiente.setCellFactory(col -> new EditableIntegerTableCell<Producto>());
        columnaCantidadPendiente.setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        columnaCantidadPendiente.setResizable(false);
        columnaCantidadPendiente.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Producto, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Producto, Integer> t) {
                t.getRowValue().cantPendiente = t.getNewValue();
            }
        });
        

        TableColumn<Producto,String> columnaComentarios = new TableColumn<>("Comentarios");
        columnaComentarios.setEditable(true);
        columnaComentarios.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        columnaComentarios.setCellFactory(col -> new EditableStringTableCell<Producto>());
        columnaComentarios.setPrefWidth(tablaVistaPrevia.getWidth()*0.22);
        columnaComentarios.setResizable(false);
        columnaComentarios.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Producto, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Producto, String> t) {
                t.getRowValue().comentario = t.getNewValue();
            }
        });
        

        tablaVistaPrevia.getColumns().addAll(columnaProducto, columnaUnidad, columnaCantidadPedida, columnaCantidadEntregada, columnaCantidadPendiente, columnaComentarios);
    }

}    