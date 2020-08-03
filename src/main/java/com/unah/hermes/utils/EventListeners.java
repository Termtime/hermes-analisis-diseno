package com.unah.hermes.utils;

import java.util.function.Consumer;
import java.util.function.Function;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;

public class EventListeners {

    /**
     * Ejecuta una funcion cuando se ha abierto la pagina y sus componentes han sido renderizados
     * @param rootPane un AnchorPane, por lo general se recomienda que sea el elemento root del .fxml
     * @param metodo es un metodo para ejecutar
     */
    public static void onWindowOpened(AnchorPane rootPane, Function<Window,Void> metodo){
        rootPane.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
              newValue.windowProperty().addListener(new ChangeListener<Window>() {
                @Override
                public void changed(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {
                    Window parent = newValue;
                    newValue.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        try{
                            metodo.apply(parent);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            System.out.println("WINDOW OPENED - NO SE PUDO BINDEAR A " + rootPane.getId());
                        }
                  }});
                }
              });
            }
        });
    }

    /**
     * Ejecuta una funcion cuando se ha empezado la destruccion de la página y sus componentes estan por destruirse
     * @param rootPane un AnchorPane, por lo general se recomienda que sea el elemento root del .fxml
     * @param metodo es un metodo para ejecutar
     */
    public static void onWindowClosed(AnchorPane rootPane, Function<Window,Void> metodo){
        rootPane.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
              newValue.windowProperty().addListener(new ChangeListener<Window>() {
                @Override
                public void changed(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {
                    Window parent = newValue;
                    newValue.addEventHandler(WindowEvent.WINDOW_HIDDEN, new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        try{
                            metodo.apply(parent);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            System.out.println("WINDOW HIDDEN - NO SE PUDO BINDEAR A " + rootPane.getId());
                        }
                  }});
                }
              });
            }
        });
    }

    /**
     * Ejecuta una funcion cuando se ha cerrado la pagina y sus componentes han sido destruidos
     * @param rootPane un AnchorPane, por lo general se recomienda que sea el elemento root del .fxml
     * @param metodo es un metodo para ejecutar
     */
    public static void onWindowClosing(AnchorPane rootPane, Function<Window,Void> metodo){
        rootPane.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
              newValue.windowProperty().addListener(new ChangeListener<Window>() {
                @Override
                public void changed(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {
                    Window parent = newValue;
                    newValue.addEventHandler(WindowEvent.WINDOW_HIDING, new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        try{
                            metodo.apply(parent);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            System.out.println("WINDOW HIDING - NO SE PUDO BINDEAR A " + rootPane.getId());
                        }
                  }});
                }
              });
            }
        });
    }
    
}