package com.unah.hermes.provider;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.EventListener;
import com.google.gson.Gson;
import com.unah.hermes.MainPageController;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.utils.ParameterStringBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;

public class FirebaseConnector{
    private Firestore db;
    private FirebaseApp app;
    private FirebaseAuth auth;
    private String API_KEY;
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
            final JSONParser jsonParser = new JSONParser();
            final FileReader file = new FileReader("api_key.json");
            final Object obj  = jsonParser.parse(file);
            final JSONObject apiKey = (JSONObject) obj;
            API_KEY = (String) apiKey.get("api_key");
            final FileInputStream serviceAccount = new FileInputStream("hermes-proyecto-is702-firebase-adminsdk-development.json");
            final FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://hermes-proyecto-is702.firebaseio.com")
            .build();
            app = FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore(app);
            auth = FirebaseAuth.getInstance(app);

            System.out.println("FINISHED");
            System.out.println(db);

        } catch(final Exception e){
            System.out.println(e);
        }
    }

    //FIRESTORE METHODS
    public boolean createDocument(final String databaseCollectionPath, final Map<String, Object> data)
    {
        try{
            final ApiFuture<DocumentReference> query = db.collection(databaseCollectionPath).add(data);
            //execute query
            query.get();
            return true;
        }catch(final Exception e)
        {
            System.out.println(e);
            //query did not execute correctly
            return false;
        }
    }

    public List<QueryDocumentSnapshot> getAllDocumentsFrom(final String databaseCollectionPath) 
    {
        try{
            final ApiFuture<QuerySnapshot> query = db.collection(databaseCollectionPath).get();
            // query.get() blocks on response
            final QuerySnapshot querySnapshot = query.get();
            final List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            return documents;
        }catch(final Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public DocumentSnapshot getDocumentFrom(final String databaseCollectionPath, final String documentID)
    {
        try{
            final ApiFuture<DocumentSnapshot> query = db.collection(databaseCollectionPath).document(documentID).get();
            // query.get() blocks on response
            final DocumentSnapshot queriedDoc = query.get();

            return queriedDoc;
        }catch(final Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public List<QueryDocumentSnapshot> queryWhereOperation(final String databaseCollectionPath, final String field, final String operation,  final Object value) 
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
            final ApiFuture<QuerySnapshot> querySnapshot = query.get();
            final List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            return documents;
        }catch(final Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public boolean deleteDocument(final String databaseCollectionPath, final String documentID) 
    {
        try{
            final ApiFuture<WriteResult> query = db.collection(databaseCollectionPath).document(documentID).delete();
            // query.get() blocks on response
            //execute query
            query.get();
            return true;
        }catch(final Exception e)
        {
            System.out.println(e);
            //query did not execute correctly
            return false;
        }
    }

    public boolean updateDocument(final String databaseCollectionPath, final String documentID, final Map<String, Object> newData)
    {
        try{
            final ApiFuture<WriteResult> query = db.collection(databaseCollectionPath).document(documentID).update(newData);
            //execute query
            query.get();
            return true;
        }catch(final Exception e)
        {
            System.out.println(e);
            //query did not execute correctly
            return false;
        }
    }

    public void iniciarListenerRequisiciones(){
        try{
            CollectionReference docRef = db.collection("Requisiciones");
            docRef.addSnapshotListener(new EventListener<QuerySnapshot>(){
                @Override
                public void onEvent( QuerySnapshot snapshots, FirestoreException e) {
                    if (e != null) {
                        System.err.println("Listen failed:" + e);
                        return;
                    }

                    MainPageController.RequisicionesDenegadas.clear();
                    MainPageController.RequisicionesEntregadas.clear();
                    MainPageController.RequisicionesPendientes.clear();

                    for (DocumentSnapshot doc : snapshots) {
                        System.out.println(doc);
                        Requisicion tmp;
                        if(doc.exists()){

                            tmp = new Requisicion(doc.getId(), doc.getString("nombreDisplay"), doc.getString("estado"),
                            doc.getString("area"), doc.getString("autorizador"), doc.getBoolean("autorizacion"), doc.getString("solicitante"), doc.getDate("fecha"), doc.get("productos"));

                            System.out.println(tmp.estado);
                            System.out.println(doc.getData());
                            if (tmp.estado.equals("Entregada")) {
                                MainPageController.RequisicionesEntregadas.add(tmp);
                                System.out.println(MainPageController.RequisicionesEntregadas);
                            }
                            else if (tmp.estado.equals("Denegada")) {
                                MainPageController.RequisicionesDenegadas.add(tmp);
                                System.out.println(MainPageController.RequisicionesDenegadas);
                            }
                            else if (tmp.estado.equals("Pendiente")) {
                                MainPageController.RequisicionesPendientes.add(tmp);
                                System.out.println(MainPageController.RequisicionesPendientes);
                            }else{
                                System.out.println("Estado desconocido");
                            }
                        }
                    }
                }
            });    
        }catch(Exception e){
            e.printStackTrace();
        }
    };
    //AUTH METHODS
 
    public boolean loginWithEmailPassword(final String email, final String password)
    {
        final String API_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;
        try{
            int status;
            final URL url = new URL(API_ENDPOINT);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            final Map<String, String> requestParameters = new HashMap<>();
            requestParameters.put("email", email);
            requestParameters.put("password", password);

            con.setDoOutput(true);
            final DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(requestParameters));
            
            status = con.getResponseCode();
            InputStream in;
            Map<String, Object> httpResponse;
            
            if(status > 299)
            {
                in = con.getErrorStream();
                httpResponse = getJSONfromHttpStream(con, in);
                con.disconnect();
                System.out.println(status);
                System.out.println("FAIL");
                System.out.println( ( (Map<String, Object>) (httpResponse.get("error"))).get("message") );
                return false;
            }
            else {
                in = con.getInputStream();
                httpResponse = getJSONfromHttpStream(con, in);
                System.out.println("PASS");
                con.disconnect();
                return true;
            } 

        }catch(final IOException e )
        {   
            System.out.println(e);
            System.out.println("FAIL");
            return false;
        }
        
    }

    public Map<String, Object> getJSONfromHttpStream(HttpURLConnection con, InputStream in)
    {
        try{
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            final String body = IOUtils.toString(in, encoding);
            System.out.println(body);
            
            final Map<String, Object> retMap = new Gson().fromJson(
                body, new TypeToken<HashMap<String, Object>>() {}.getType()
            );

            return retMap;
        }catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
        
        
    }
}
