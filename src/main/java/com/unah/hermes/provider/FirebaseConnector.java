package com.unah.hermes.provider;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;

public class FirebaseConnector{
    Firestore db;
    FirebaseApp app;
    //Patron SINGLETON
    private static final FirebaseConnector instance = new FirebaseConnector();

    //iniciador de la instancia
    private FirebaseConnector(){
        initFirebase();
    }

    //Debemos utilizar este constructor para recuperar la instancia de la clase
    public static FirebaseConnector getInstance()
    {
        return instance;
    }

    private void initFirebase()
    {
        try
        {
            FileInputStream serviceAccount = new FileInputStream("hermes-proyecto-is702-firebase-adminsdk-development.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://hermes-proyecto-is702.firebaseio.com")
            .build();
            app = FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();

            System.out.println("FINISHED");
            System.out.println(db);

        } catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean createDocument(String databaseCollectionPath, Map<String, Object> data)
    {
        try{
            ApiFuture<DocumentReference> query = db.collection(databaseCollectionPath).add(data);
            //execute query
            query.get();
            return true;
        }catch(Exception e)
        {
            System.out.println(e);
            //query did not execute correctly
            return false;
        }
    }

    public List<QueryDocumentSnapshot> getAllDocumentsFrom(String databaseCollectionPath) 
    {
        try{
            ApiFuture<QuerySnapshot> query = db.collection(databaseCollectionPath).get();
            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            return documents;
        }catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public List<QueryDocumentSnapshot> queryWhereOperation(String databaseCollectionPath, String operation, String field, Object value) 
    {
        try{
            Query query;

            switch(operation)
            {
                case ">":
                    query = db.collection(databaseCollectionPath).whereGreaterThan(field, value);        
                break;

                case "<":
                    query = db.collection(databaseCollectionPath).whereLessThan(field, value);
                break;

                case ">=":
                    query = db.collection(databaseCollectionPath).whereGreaterThanOrEqualTo(field, value);
                break;

                case "<=":
                    query = db.collection(databaseCollectionPath).whereLessThanOrEqualTo(field, value);
                break;

                case "=":
                    query = db.collection(databaseCollectionPath).whereEqualTo(field, value);
                break;

                default:
                    System.out.println("Operacion no soportada");
                    return null;
            }
            
            // query.get() blocks on response
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            return documents;
        }catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public boolean deleteDocument(String databaseCollectionPath, String documentID) 
    {
        try{
            ApiFuture<WriteResult> query = db.collection(databaseCollectionPath).document(documentID).delete();
            // query.get() blocks on response
            //execute query
            query.get();
            return true;
        }catch(Exception e)
        {
            System.out.println(e);
            //query did not execute correctly
            return false;
        }
    }

    public boolean updateDocument(String databaseCollectionPath, String documentID, Map<String, Object> newData)
    {
        try{
            ApiFuture<WriteResult> query = db.collection(databaseCollectionPath).document(documentID).update(newData);
            //execute query
            query.get();
            return true;
        }catch(Exception e)
        {
            System.out.println(e);
            //query did not execute correctly
            return false;
        }
    }
    
}
