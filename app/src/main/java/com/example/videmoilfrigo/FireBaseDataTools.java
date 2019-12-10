package com.example.videmoilfrigo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public Map<String, Object> get_document_in_map(final String p_collection, final String p_document){

        DocumentReference docRef = db.collection(p_collection).document(p_document);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_USER", "DocumentSnapshot data: " + document.getData());
                        data.clear();
                        data = document.getData();
                    } else {
                        Log.d("GET_USER", "No such document");
                    }
                } else {
                    Log.d("GET_USER", "get failed with ", task.getException());
                }
            }
        });

        return data;
    }
}
