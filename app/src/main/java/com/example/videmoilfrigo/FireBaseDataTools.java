package com.example.videmoilfrigo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class FireBaseDataTools {

    private static FireBaseDataTools fire_base_data_tools = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> data = new HashMap<>();

    private FireBaseDataTools(){}

    public static FireBaseDataTools get_instance(){
        if(fire_base_data_tools == null){
            fire_base_data_tools = new FireBaseDataTools();
        }

        return fire_base_data_tools;
    }

    public void ecoute_frigo(){
        if(C_user.get_instance().get_currentUser().getUid() != null) {
            final DocumentReference docRef = db.collection("frigo").
                    document(C_user.get_instance().get_currentUser().getUid());
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("ecoute_frigo", "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("ecoute_frigo", "Current data: " + snapshot.getData());
                        data = snapshot.getData();
                    } else {
                        Log.d("ecoute_frigo", "Current data: null");
                    }
                }
            });
        }
    }

    public void add_map_in_document(final String p_collection, final String p_document, Map<String, Object> p_data){

        // Add a new document with a generated ID
        Log.d("TEST_add_user","add_user");
        db.collection(p_collection)
                .document(p_document)
                .set(p_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("add_map_in_document", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("add_map_in_document", "Error adding document", e);
                    }
                });
    }

    public void add_map_in_document(final String p_collection, final String p_sub_collection, final String p_document, Map<String, Object> p_data){

        // Add a new document with a generated ID
        Log.d("TEST_add_user","add_user");
        db.collection(p_collection)
                .document(p_document)
                .collection(p_sub_collection)
                .add(p_data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("add_map_in_document", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("add_map_in_document", "Error adding document", e);
                    }
                });
    }

    public Map<String, Object> get_data(){
        return data;
    }
}
