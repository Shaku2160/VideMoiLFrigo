package com.example.videmoilfrigo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class C_user {
    private FirebaseAuth mAuth = null;
    private static C_user utilisateur = null;
    FirebaseUser currentUser = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String nom_user;
    private String prenom_user;

    private C_user(){}

    public static C_user get_instance(){
        if(utilisateur == null){
            utilisateur = new C_user();
        }

        return utilisateur;
    }

    public void set_mAuth(FirebaseAuth p_mAuth)
    {
        mAuth = p_mAuth;
    }

    public FirebaseAuth get_mAuth()
    {
        return mAuth;
    }

    public void set_currentUser(FirebaseUser p_currentUser)
    {
        currentUser = p_currentUser;
    }

    public FirebaseUser get_currentUser()
    {
        return currentUser;
    }

    public void add_user(final String p_nom, final String p_prenom){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", p_prenom);
        user.put("last", p_nom);
        nom_user = p_nom;
        prenom_user = p_prenom;

        // Add a new document with a generated ID
        Log.d("TEST_add_user","add_user");
        db.collection("users")
                .document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ADD_USER", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR", "Error adding document", e);
                    }
                });
    }

    public void get_user()
    {
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_USER", "DocumentSnapshot data: " + document.getData());
                        prenom_user = document.getData().get("first").toString();
                        nom_user = document.getData().get("last").toString();
                    } else {
                        Log.d("GET_USER", "No such document");
                    }
                } else {
                    Log.d("GET_USER", "get failed with ", task.getException());
                }
            }
        });
    }

    public String get_nom_user() {
        return nom_user;
    }

    public String get_prenom_user() {
        return prenom_user;
    }

}
