package com.unah.hermes.tests;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FirestoreTestController implements Initializable {
    
    
    FirebaseConnector connector;

    @FXML
    private Label label;
    
    @FXML
    private void getAreas(ActionEvent event) {
        List<QueryDocumentSnapshot> results = connector.getAllDocumentsFrom(FirestoreRoutes.AREAS);

        for (QueryDocumentSnapshot document : results) {
            System.out.println(document.getString("Area"));
        }

        System.out.println("OPERATION DONE");
    }
    
    @FXML
    private void createTestArea(ActionEvent event){
        Map<String,Object> data = new HashMap<>();
        Random r = new Random();
        data.put("area", "Prueba");
        data.put("rating", r.nextInt(10));
        connector.createDocument(FirestoreRoutes.AREAS, data);

        System.out.println("OPERATION DONE");
    }

    @FXML
    private void greaterThan(ActionEvent event) {
        List<QueryDocumentSnapshot> results = connector.queryWhereOperation(FirestoreRoutes.AREAS, ">", "rating", 4);
        for (QueryDocumentSnapshot document : results) {
            System.out.println(document.getString("Area"));
            System.out.println(document.getDouble("rating"));
        }

        System.out.println("OPERATION DONE");
    }

    @FXML
    private void greaterOrWhere(ActionEvent event) {
        List<QueryDocumentSnapshot> results = connector.queryWhereOperation(FirestoreRoutes.AREAS, ">=", "rating", 4);
        for (QueryDocumentSnapshot document : results) {
            System.out.println(document.getString("Area"));
            System.out.println(document.getDouble("rating"));
        }

        System.out.println("OPERATION DONE");
    }

    @FXML
    private void lessOrWhere(ActionEvent event) {
        List<QueryDocumentSnapshot> results = connector.queryWhereOperation(FirestoreRoutes.AREAS, "<=", "rating", 4);
        for (QueryDocumentSnapshot document : results) {
            System.out.println(document.getString("Area"));
            System.out.println(document.getDouble("rating"));
        }

        System.out.println("OPERATION DONE");
    }

    @FXML
    private void lessWhere(ActionEvent event) {
        List<QueryDocumentSnapshot> results = connector.queryWhereOperation(FirestoreRoutes.AREAS, "<", "rating", 4);
        for (QueryDocumentSnapshot document : results) {
            System.out.println(document.getString("Area"));
            System.out.println(document.getDouble("rating"));
        }

        System.out.println("OPERATION DONE");
    }

    @FXML
    private void equalsWhere(ActionEvent event) {
        List<QueryDocumentSnapshot> results = connector.queryWhereOperation(FirestoreRoutes.AREAS, "=", "rating", 4);
        for (QueryDocumentSnapshot document : results) {
            System.out.println(document.getString("Area"));
            System.out.println(document.getDouble("rating"));
        }

        System.out.println("OPERATION DONE");
    }

    @FXML
    private void updateTest(ActionEvent event) {
        Map<String,Object> data = new HashMap<>();
        data.put("rating", 11.1234);
        connector.updateDocument(FirestoreRoutes.AREAS, "test", data);

        System.out.println("OPERATION DONE");
    }

    @FXML
    private void deleteTest(ActionEvent event) {
        connector.deleteDocument(FirestoreRoutes.AREAS, "test");

        System.out.println("OPERATION DONE");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connector = FirebaseConnector.getInstance();
    }    
}
