package com.example.videmoilfrigo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {

    private TextView email, password;
    private Button bouton_connexion, bouton_sign_in;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.bouton_connexion:
                    createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString());
                    break;
                case R.id.bouton_sign_in:
                    sign_in(email.getText().toString(), password.getText().toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        email = this.findViewById(R.id.e_mail);
        password = this.findViewById(R.id.password);
        bouton_connexion = this.findViewById(R.id.bouton_connexion);
        bouton_sign_in = this.findViewById(R.id.bouton_sign_in);
        bouton_connexion.setOnClickListener(mOnClickListener);
        bouton_sign_in.setOnClickListener(mOnClickListener);
    }

    private void createUserWithEmailAndPassword(String p_email, final String p_password) {
        C_user.get_instance().get_mAuth().createUserWithEmailAndPassword(p_email, p_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("CREATE_USER", "createUserWithEmail:success");
                            FirebaseUser user = C_user.get_instance().get_mAuth().getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("CREATE_USER", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Main2Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void sign_in(String p_email, final String p_password)
    {
        C_user.get_instance().get_mAuth().signInWithEmailAndPassword(p_email, p_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGN_IN", "signInWithEmail:success");
                            FirebaseUser user = C_user.get_instance().get_mAuth().getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGN_IN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Main2Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null)
        {
            Intent connexionActivity = new Intent(this, MainActivity.class);
            startActivity(connexionActivity);
        }
    }
}
