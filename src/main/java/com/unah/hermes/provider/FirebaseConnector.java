package com.unah.hermes.provider;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.EventListener;
import com.google.gson.Gson;
import com.unah.hermes.MainPage;
import com.unah.hermes.objects.Area;
import com.unah.hermes.MantUsuariosPage;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.objects.User;
import com.unah.hermes.utils.ParameterStringBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.application.Platform;
import javafx.scene.image.Image;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.FirebaseApp;

public class FirebaseConnector {
    private Firestore db;
    private FirebaseApp app;
    private FirebaseAuth auth;
    private Storage storage;
    private String API_KEY;
    private String BUCKET_NAME = "hermes-proyecto-is702.appspot.com";
    private String PROJECT_ID;
    // Patron SINGLETON
    private static final FirebaseConnector instance = new FirebaseConnector();

    // iniciador de la instancia
    private FirebaseConnector() {
        initFirebase();
    }

    // Debemos utilizar este constructor para recuperar la instancia de la clase
    public static FirebaseConnector getInstance() {
        return instance;
    }

    private void initFirebase() {
        try {
            //obtener el Api key
             JSONParser jsonParser = new JSONParser();
             FileReader file = new FileReader("api_key.json");
             Object obj = jsonParser.parse(file);
             JSONObject apiKey = (JSONObject) obj;
             API_KEY = (String) apiKey.get("api_key");
             //obtener el project ID
             FileReader adminSDK = new FileReader("hermes-proyecto-is702-firebase-adminsdk-development.json");
             Object obj2 = jsonParser.parse(adminSDK);
             JSONObject serviceAccJSON = (JSONObject) obj2;
             adminSDK.close();
             PROJECT_ID = serviceAccJSON.get("project_id").toString();

            //obtener el project ID

            final FileInputStream firestorage = new FileInputStream("hermes-proyecto-is702-firebase-adminsdk-development.json");
            final FileInputStream serviceAccount = new FileInputStream("hermes-proyecto-is702-firebase-adminsdk-development.json");
            final StorageOptions firestorageOptions = StorageOptions.newBuilder()
                .setProjectId(PROJECT_ID)
                .setCredentials(GoogleCredentials.fromStream(firestorage)).build();

            final FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(BUCKET_NAME)
                .setProjectId(PROJECT_ID)
                .setDatabaseUrl("https://hermes-proyecto-is702.firebaseio.com").build();
            app = FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore(app);
            auth = FirebaseAuth.getInstance(app);
            storage =  firestorageOptions.getService();
            System.out.println("FINISHED LOADING");
            // System.out.println(db);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permite crear un documento en la base de datos
     * 
     * @param databaseCollectionPath Es la ruta de la coleccion de la base de datos
     *                               en la cual crear el documento
     * @param data                   Es un mapa de datos, Ej; {reqID: '1', area :
     *                               'cocina'}
     * 
     * @return Booleano que indica si el documento fue creado exitosamente
     */
    // FIRESTORE METHODS
    public boolean createDocument(final String databaseCollectionPath, final Map<String, Object> data) {

        try {
            final ApiFuture<DocumentReference> query = db.collection(databaseCollectionPath).add(data);
            // execute query
            query.get();
            return true;
        } catch (final Exception e) {
            System.out.println(e);
            // query did not execute correctly
            return false;
        }
    }

    /**
     * Elimina un documento de la base de datos
     * 
     * @param databaseCollectionPath Es la ruta de la coleccion de la base de datos
     *                               en la cual eliminar el documento
     * @param documentID             Es el ID del documento a eliminar
     * 
     * @return Booleano que indica si el documento fue eliminado exitosamente
     */
    public boolean deleteDocument(final String databaseCollectionPath, final String documentID) {
        try {
            final ApiFuture<WriteResult> query = db.collection(databaseCollectionPath).document(documentID).delete();
            // query.get() blocks on response
            // execute query
            query.get();
            return true;
        } catch (final Exception e) {
            System.out.println(e);
            // query did not execute correctly
            return false;
        }
    }

    /**
     * Actualiza un documento con nuevos datos
     * 
     * @param databaseCollectionPath Es la ruta de la coleccion de la base de datos
     *                               en la cual actualizar el documento
     * @param documentID             Es el ID del documento a actualizar
     * @param newData                son los nuevos datos a ingresar o actualizar,
     *                               si se envia un dato que no existe, se agrega,
     *                               si se envia uno que ya existe, se actualiza
     * 
     * @return Booleano que indica si el documento fue actualizado exitosamente
     */
    public boolean updateDocument(final String databaseCollectionPath, final String documentID,
            final Map<String, Object> newData) {
        try {
            final ApiFuture<WriteResult> query = db.collection(databaseCollectionPath).document(documentID)
                    .update(newData);
            // execute query
            query.get();
            return true;
        } catch (final Exception e) {
            System.out.println(e);
            // query did not execute correctly
            return false;
        }
    }

    /**
     * Obtiene todos los documentos dentro de una coleccion
     * 
     * @param databaseCollectionPath Es la ruta de la coleccion de la base de datos
     *                               de la cual se desea obtener documentos
     * 
     * @return Una lista de QueryDocumentSnapshot sin procesar si se realiza
     *         correctamente, sino un Null
     */
    public List<QueryDocumentSnapshot> getAllDocumentsFrom(final String databaseCollectionPath) {
        try {
            final ApiFuture<QuerySnapshot> query = db.collection(databaseCollectionPath).get();
            // query.get() blocks on response
            final QuerySnapshot querySnapshot = query.get();
            final List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            return documents;
        } catch (final Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Obtiene un solo documento
     * 
     * @param databaseCollectionPath Es la ruta de la coleccion de la base de datos
     *                               de la cual se desea obtener documentos
     * @param documentID             Es el ID del documento a obtener
     * 
     * @return Un DocumentSnapshot si se obtuvo correctamente, sino un Null
     */
    public DocumentSnapshot getDocumentFrom(final String databaseCollectionPath, final String documentID) {
        try {
            final ApiFuture<DocumentSnapshot> query = db.collection(databaseCollectionPath).document(documentID).get();
            // query.get() blocks on response
            final DocumentSnapshot queriedDoc = query.get();

            return queriedDoc;
        } catch (final Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Realiza un query Where en la base de datos Por ejemplo si se desea encontrar
     * los productos que tienen por unidad la Libra:
     * queryWhereOperation("/Productos", "unidad", "=", "Libra")
     * 
     * @param databaseCollectionPath Es la ruta de la coleccion de la base de datos
     *                               de la cual se desea obtener documentos
     * @param field                  Campo de algun objeto en la coleccion
     * @param operation              Es un string de operacion, solamente:
     *                               {@code "<", ">", "=", "<=", ">="}
     * @param value                  Es el valor a comparar
     * 
     * @return Un DocumentSnapshot si se obtuvo correctamente, sino un Null
     */
    public List<QueryDocumentSnapshot> queryWhereOperation(final String databaseCollectionPath, final String field,
            final String operation, final Object value) {

        try {
            Query query;

            switch (operation) {
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
        } catch (final Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Inicia un Listener de requisiciones
     * 
     * @return Una funcion para desuscribir el snapshot listener, es importante
     *         llamar esta funcion cuando se destruya la ventana
     */
    public ListenerRegistration iniciarListenerRequisiciones() {
        try {
            CollectionReference docRef = db.collection("Requisiciones");
            ListenerRegistration unsubListener = docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot snapshots, FirestoreException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (e != null) {
                                System.err.println("Listen failed:" + e);
                                return;
                            }
        
                            MainPage.RequisicionesDenegadas.clear();
                            MainPage.RequisicionesEntregadas.clear();
                            MainPage.RequisicionesPendientes.clear();
        
                            for (DocumentSnapshot doc : snapshots) {
                                // System.out.println(doc);
                                Requisicion tmp;
                                if (doc.exists()) {
        
                                    tmp = new Requisicion(doc.getId(), doc.getString("nombreDisplay"), doc.getString("estado"),
                                            doc.getString("area"), doc.getString("autorizador"), doc.getBoolean("autorizacion"),
                                            doc.getString("solicitante"), doc.getDate("fecha"), doc.get("productos"));
        
                                    // System.out.println(tmp.estado);
                                    // System.out.println(doc.getData());
                                    if (tmp.estado.equals("Entregada")) {
                                        MainPage.RequisicionesEntregadas.add(tmp);
                                        // System.out.println(MainPage.RequisicionesEntregadas);
                                    } else if (tmp.estado.equals("Denegada")) {
                                        MainPage.RequisicionesDenegadas.add(tmp);
                                        // System.out.println(MainPage.RequisicionesDenegadas);
                                    } else if (tmp.estado.equals("Pendiente")) {
                                        MainPage.RequisicionesPendientes.add(tmp);
                                        // System.out.println(MainPage.RequisicionesPendientes);
                                    } else {
                                        System.out.println("Estado desconocido");
                                    }
                                }
                            }
                        }
                    });
                    
                }
            });
            return unsubListener;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };
    // AUTH METHODS

    public String loginWithEmailPassword(final String email, final String password) {
        final String API_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                + API_KEY;
        try {
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

            if (status > 299) {
                in = con.getErrorStream();
                httpResponse = getJSONfromHttpStream(con, in);
                con.disconnect();
                System.out.println(status);
                System.out.println("FAIL");
                System.out.println(((Map<String, Object>) (httpResponse.get("error"))).get("message"));
                return ((Map<String, Object>) (httpResponse.get("error"))).get("message").toString();
            } else {
                in = con.getInputStream();
                httpResponse = getJSONfromHttpStream(con, in);
                System.out.println("PASS");
                con.disconnect();
                return "PASS";
            }

        } catch (final IOException e) {
            System.out.println(e);
            System.out.println("FAIL");
            return "ERROR";
        }

    }

    /**
     * Crea el usuario en firebase auth y firestore
     * 
     * @param email       Correo del usuario
     * @param password    Contraseña del usuario
     * @param nombre      El nombre del usuario
     * @param nivelAcceso El nivel de acceso del usuario
     * @param areas       Lista de Objetos tipo Area
     * @return True si la creacion fue exitosa, sino retorna false
     */
    public boolean crearUsuario(String email, String password, String nombre, String nivelAcceso, List<Area> areas) {
        try {
            // crear el usuario en firebase auth
            CreateRequest request = new CreateRequest().setEmail(email).setPassword(password).setDisplayName(nombre);
            UserRecord nuevoUsuario = auth.createUser(request);

            // crear el usuario en firestore
            List<String> areasID = new ArrayList<>();
            for (Area area : areas) {
                areasID.add(area.areaID);
            }
            Map<String, Object> datos = new HashMap<>();
            datos.put("Nombre", nombre);
            datos.put("nivelAcceso", nivelAcceso);
            datos.put("areas", areasID);
            datos.put("uid", nuevoUsuario.getUid());
            // ejecutar la instruccion en firebase
            db.collection("Usuarios").document(email).set(datos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifica el usuario en firestore y fire
     * 
     * @param uid         El User ID de Firebase Auth, almacenado en firestore
     *                    tambien
     * @param email       Correo del usuario
     * @param nombre      El nombre del usuario
     * @param nivelAcceso El nivel de acceso del usuario
     * @param areas       Lista de Objetos tipo Area, debe ser una lista que
     *                    contenga las areas actuales y las nuevas
     * @return True si la modificacion fue exitosa, sino false
     */
    public boolean modificarUsuario(String uid, String email, String nombre, String nivelAcceso,
            List<Area> areas) {
        try {
            UpdateRequest request = new UpdateRequest(uid).setDisplayName(nombre);
            auth.updateUser(request);
            // modificar el usuario en firestore
            List<String> areasID = new ArrayList<>();
            for (Area area : areas) {
                areasID.add(area.areaID);
            }

            Map<String, Object> datos = new HashMap<>();
            datos.put("Nombre", nombre);
            datos.put("nivelAcceso", nivelAcceso);
            datos.put("areas", areasID);
            // ejecutar la instruccion en firebase
            db.collection("Usuarios").document(email).set(datos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean eliminarUsuario(String uid, String firestoreDocumentID) {
        try {
            auth.deleteUser(uid);
            deleteDocument(FirestoreRoutes.USUARIOS, firestoreDocumentID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarPassword(String uid, String password) {
        UpdateRequest request = new UpdateRequest(uid).setPassword(password);
        try {
            auth.updateUser(request);
            return true;
        } catch (FirebaseAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    public Map<String, Object> getJSONfromHttpStream(HttpURLConnection con, InputStream in)
    {
        try{
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            final String body = IOUtils.toString(in, encoding);
            // System.out.println(body);
            
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

    //Google cloud storage methods
    public boolean uploadImage(String firestoragePath, String filePath){
        System.out.println("Subiendo archivo");
        String objectName= Paths.get(filePath).getFileName().toString();
        try {
            String mimeType = Files.probeContentType(Paths.get(filePath));
            //si el archivo seleccionado no es una imagen png o jpeg no proseguir
            if(!mimeType.equals("image/png") && !mimeType.equals("image/jpeg")) return false;
            // Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            BlobId blobId = BlobId.of(BUCKET_NAME, firestoragePath+objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(mimeType)
                .build();
            storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
        
            System.out.println(
                "File " + filePath + " uploaded to bucket " + BUCKET_NAME + " as " + objectName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error subiendo archivo");
            return false;
        }
    }

    public Image downloadImage(String firestoragePath, String objectName){
        try {
            //obtener el archivo, crear un canal de lectura y un input stream para crear una imagen
            Blob blob = storage.get(BUCKET_NAME, firestoragePath + objectName);
            ReadChannel readChannel = blob.reader();
            InputStream inputStream = Channels.newInputStream(readChannel);
            Image imagen = new Image(inputStream);

            return imagen;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error bajando archivo");
            return null;
        }
    }
}
