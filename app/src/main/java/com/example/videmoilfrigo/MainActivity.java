package com.example.videmoilfrigo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_liste,
                R.id.navigation_connexion)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Initialize Firebase Auth
        C_user.get_instance().set_mAuth(FirebaseAuth.getInstance());


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        C_user.get_instance().set_currentUser(C_user.get_instance().get_mAuth().getCurrentUser());
        if(C_user.get_instance().get_currentUser() != null)
        {
            C_user.get_instance().get_user();
        }
        updateUI(C_user.get_instance().get_currentUser());
    }

    private void updateUI(FirebaseUser currentUser) {


        if (currentUser != null) {
            Toast.makeText(this, "Authentication failed ?",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Authentication success ?",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
