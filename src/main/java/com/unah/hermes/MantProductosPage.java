package com.unah.hermes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.objects.Categoria;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestorageRoutes;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import javafx.application.Platform;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
public class MantProductosPage implements Initializable {
    
    @FXML private void btnAgregarProductoClick(final ActionEvent event) {
        Navigation.pushRoute("MantProductosModalAgregarProducto", event, false, true);
        refreshProductos();
    }
    @FXML private void btnModificarProductoClick(final ActionEvent event) {
        if(tablaProductoSelectedItem != null)
            Navigation.pushRouteWithParameter("MantProductosModalModificarProducto", event, false, true, MantProductosModalModificarProducto.class, tablaProductoSelectedItem );
        else{
            Navigation.pushRoute("AlertError", event, false, true);
        }
        refreshProductos();
    }
    @FXML private void btnEliminarProductoClick(final ActionEvent event) {
        if(tablaProductos.getSelectionModel().getSelectedItem()==null){
            Navigation.mostrarAlertError("Debe selecionar un producto", event);
            return;
        }

        if(!isShiftDown)
            if(!Navigation.mostrarAlertConfirmacion("¿Desea eliminar el producto '" + tablaProductos.getSelectionModel().getSelectedItem().nombre + "' ?", event)) return;
        for (final DocumentSnapshot doc : documentos) {
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
    @FXML private void btnAgregarCategoriaClick(final ActionEvent event) {
        Navigation.pushRoute("MantProductosModalAgregarCategoria", event, false, true);
        refreshCategorias();
        refreshProductos();
    }
    @FXML private void btnModificarCategoriaClick(final ActionEvent event) {
        Navigation.pushRoute("MantProductosModalModificarCategoria", event, false, true);
        refreshCategorias();
        refreshProductos();
    }
    @FXML private void btnEliminarCategoriaClick(final ActionEvent event) {
        if(comboCategoria.getSelectionModel().isSelected(-1)||comboCategoria.getSelectionModel().isSelected(0)){
            Navigation.mostrarAlertError("Debe seleccionar una Categoria", event);
            return;
        }
        //confirmar si se desea cerrar sesión solo si no se está sosteniendo shift
        if(!isShiftDown)
            if(!Navigation.mostrarAlertConfirmacion("¿Desea eliminar la Categoria '"+ comboCategoria.getSelectionModel().getSelectedItem() + "' ?", event)) return;
        for (final DocumentSnapshot doc : categoriaDocumentos) {
            Categoria tmp;
            if(doc.exists()){
                tmp = new Categoria(doc.getId(),doc.getString("nombre"));
                if(tmp.Nombre!=null)
                {
                    if(tmp.Nombre.equals(comboCategoria.getSelectionModel().getSelectedItem().toString()))
                    {
                        db.deleteDocument("Categorias", tmp.CategoriaID);
                        break;
                    }
                }
            }
        }
        refreshCategorias();
        refreshProductos();
    }
    @FXML private void txtFiltroInput(final KeyEvent event) {
        tablaProductos.getItems().clear();
        final List<Producto> productosFiltrados = new ArrayList<Producto>();
        for(final Producto producto: productos){
            if(producto.nombre.toLowerCase().contains(txtFiltro.getText().toLowerCase()) || txtFiltro.getText().equals("")){
                productosFiltrados.add(producto);
            }    
        }
        tablaProductos.getItems().addAll(productosFiltrados);
    }
    @FXML private void comboCategoriaClick(final ActionEvent event) {
        tablaProductos.getItems().clear();
        final List<Producto> categoriaFiltrados = new ArrayList<Producto>();
        for(final Producto producto: productos){
            if(producto.categoria.toLowerCase().equals(comboCategoria.getSelectionModel().getSelectedItem().toString().toLowerCase())){
                categoriaFiltrados.add(producto);
            }    
        }
        tablaProductos.getItems().addAll(categoriaFiltrados);
    }
    @FXML private TableView<Producto> tablaProductos;
    @FXML private AnchorPane MantenimientoProductos;
    @FXML private TextField txtFiltro;
    @FXML private ComboBox<String> comboCategoria= new ComboBox<>();
    @FXML private ImageView imagenProducto;
    @FXML private Rectangle marco;
    @FXML private Button btnEliminarCategoria;
    @FXML private Button btnEliminarProducto;

    Boolean isShiftDown = false;
    Producto tablaProductoSelectedItem;
    FirebaseConnector db=FirebaseConnector.getInstance();   
    ObservableList<Producto> productos = FXCollections.observableArrayList();
    List<QueryDocumentSnapshot> documentos = db.getAllDocumentsFrom(FirestoreRoutes.PRODUCTOS);
    List<QueryDocumentSnapshot> categoriaDocumentos = db.getAllDocumentsFrom(FirestoreRoutes.CATEGORIAS);

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        bindBotonesConfirmar();
        MantenimientoProductos.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(final MouseEvent event) {
                MantenimientoProductos.requestFocus();
            }
        });
        EventListeners.onWindowOpened(MantenimientoProductos, new Function<Window,Void>(){
            @Override
            public Void apply(final Window parent) {
                iniciarEstructuraTablas();
                for (final DocumentSnapshot doc : documentos) {
                    Producto tmp;
                    if(doc.exists()){
                        tmp = new Producto(doc.getId(), doc.getString("Producto"), doc.getString("Unidad"), doc.getString("Categoria"));
                        productos.add(tmp);
                    }
                }
                tablaProductos.getItems().addAll(productos);
                for(final DocumentSnapshot cat: categoriaDocumentos){
                    Categoria tmp;
                    if(cat.exists()){
                        tmp=new Categoria(cat.getId(),cat.getString("nombre"));
                        System.out.println(tmp.getNombre());
                        comboCategoria.getItems().add(tmp.getNombre());
                    }
                }
                //escuchar cuando se sostiene shift para hacer override a los dialogos de confirmar
                parent.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                    System.out.println("key pressed");
                    if (event.isShiftDown()) {
                        isShiftDown = true;
                    }else{
                        isShiftDown = false;
                    }
                    // event.consume();
                });
                parent.getScene().addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                    System.out.println("key released");
                    if (event.isShiftDown()) {
                        isShiftDown = true;
                    }else{
                        isShiftDown = false;
                    }
                    // event.consume();
                });
                 return null;
            }
           
        });
        MantenimientoProductos.widthProperty().addListener(evt -> recalcularColumnWidth());

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

                if(newValue == null) return;
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        Image image = db.downloadImage(FirestorageRoutes.PRODUCTOS,"Productos"+newValue.productoID);
                        //si no tiene imagen, revertir al icono por defecto
                        if(image == null){
                            image =  new Image(getClass().getResourceAsStream("/images/productos.png"));
                        }
                        final Image imagenEscogida = image;
                        Platform.runLater(new Runnable(){

                            @Override
                            public void run() {
                                imagenProducto.setImage(imagenEscogida);
                                double aspectRatio = imagenEscogida.getWidth() / imagenEscogida.getHeight();
                                double realWidth = Math.min(imagenProducto.getFitWidth(), imagenProducto.getFitHeight() * aspectRatio);
                                double realHeight = Math.min(imagenProducto.getFitHeight(), imagenProducto.getFitWidth() / aspectRatio);
                                imagenProducto.setTranslateX(6);
                                imagenProducto.setTranslateY(6);

                                marco.setWidth(realWidth+12);
                                marco.setHeight(realHeight+12);

                            }
                            
                        });

                    }
                    
                }).start();
            }
        });
    }  

    private void recalcularColumnWidth(){
                       
        final ObservableList columnasMantenimiento = tablaProductos.getColumns();

        ((TableColumn)( columnasMantenimiento.get(0) )).setPrefWidth(tablaProductos.getWidth()*0.35);
        ((TableColumn)( columnasMantenimiento.get(1) )).setPrefWidth(tablaProductos.getWidth()*0.35);
        ((TableColumn)( columnasMantenimiento.get(2) )).setPrefWidth(tablaProductos.getWidth()*0.30);
        
    }
    private void iniciarEstructuraTablas(){
        
         tablaProductos.getItems().clear();
         tablaProductos.getColumns().clear();
         final TableColumn columnaProducto = new TableColumn<>("Producto");
         columnaProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
         columnaProducto.setPrefWidth(tablaProductos.getWidth()*0.35);
         columnaProducto.setResizable(false);

         final TableColumn columnaCategoria = new TableColumn<>("Categoria");
         columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
         columnaCategoria.setPrefWidth(tablaProductos.getWidth()*0.30);
         columnaCategoria.setResizable(false);

         final TableColumn columnaUnidad = new TableColumn<>("Unidad");
         columnaUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
         columnaUnidad.setPrefWidth(tablaProductos.getWidth()*0.30);
         columnaUnidad.setResizable(false);
         
         tablaProductos.getColumns().addAll(columnaProducto, columnaCategoria, columnaUnidad);
     }
    private void refreshProductos(){
        tablaProductos.getItems().clear();
        productos.clear();
        documentos = db.getAllDocumentsFrom(FirestoreRoutes.PRODUCTOS);
        for (final DocumentSnapshot doc : documentos) {
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
        for(final DocumentSnapshot cat: categoriaDocumentos){
            Categoria tmp;
            if(cat.exists()){
                tmp=new Categoria(cat.getId(),cat.getString("nombre"));
                System.out.println(tmp.getNombre());
                comboCategoria.getItems().add(tmp.getNombre());
            }
        }
    }
    private void bindBotonesConfirmar() {
        btnEliminarCategoria.setOnMouseClicked(event -> {
            btnEliminarCategoria.fire();
        });
        btnEliminarProducto.setOnMouseClicked(event -> {
            btnEliminarProducto.fire();
        });
    }
}