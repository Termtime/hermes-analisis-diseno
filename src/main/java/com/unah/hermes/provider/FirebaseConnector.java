package com.unah.hermes.provider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.api.Http;
import com.google.api.client.http.HttpRequest;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.unah.hermes.utils.ParameterStringBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
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
            JSONParser jsonParser = new JSONParser();
            FileReader file = new FileReader("api_key.json");
            Object obj  = jsonParser.parse(file);
            JSONObject apiKey = (JSONObject) obj;
            API_KEY = (String) apiKey.get("api_key");
            FileInputStream serviceAccount = new FileInputStream("hermes-proyecto-is702-firebase-adminsdk-development.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://hermes-proyecto-is702.firebaseio.com")
            .build();
            app = FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore(app);
            auth = FirebaseAuth.getInstance(app);

            System.out.println("FINISHED");
            System.out.println(db);

        } catch(Exception e){
            System.out.println(e);
        }
    }

    //FIRESTORE METHODS
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


    //AUTH METHODS
 
    public boolean loginWithEmailPassword(String email, String password)
    {
        String API_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;
        try{
            int status;
            URL url = new URL(API_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            Map<String, String> requestParameters = new HashMap<>();
            requestParameters.put("email", email);
            requestParameters.put("password", password);

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(requestParameters));
            

            Scanner s = new Scanner(url.openStream());
            String inputLine = "";
            while(s.hasNext())
            {
                inputLine += s.nextLine();
            }
            
            status = con.getResponseCode();
            s.close();
            out.flush();
            out.close();
            
            if(status > 299)
            {
                con.disconnect();
                System.out.println(status);
                return false;
            }
            else{
                System.out.println(inputLine);
                con.disconnect();
                return true;
            }

        }catch(IOException e )
        {   
            System.out.println(e.getMessage());
            
            return false;
        }
        
    }
}
