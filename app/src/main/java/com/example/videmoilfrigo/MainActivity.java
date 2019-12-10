package com.example.videmoilfrigo;


import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // initialisation de la barre de menu
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
        // vérifie que l'utilisateur est connecté
        C_user.get_instance().set_currentUser(C_user.get_instance().get_mAuth().getCurrentUser());
        if(C_user.get_instance().get_currentUser() != null)
        {
            C_user.get_instance().get_user();
        }
    }



}
