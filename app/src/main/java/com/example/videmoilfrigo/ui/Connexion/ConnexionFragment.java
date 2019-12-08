package com.example.videmoilfrigo.ui.Connexion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.videmoilfrigo.C_user;
import com.example.videmoilfrigo.Main2Activity;
import com.example.videmoilfrigo.R;

public class ConnexionFragment extends Fragment {

    private ConnexionViewModel connexionViewModel;

    private Button bouton_create_account, bouton_modification;
    private TextView textView_nom, textView_prenom;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.create_account:
                    if(bouton_create_account.getText().toString().contains("Connect")) {
                        connexion();
                    }
                    else if(bouton_create_account.getText().toString().contains("Disconnect"))
                    {
                        deconnexion();
                        updateUIDisconnect();
                    }
                    break;
                case R.id.bouton_modification:
                    if(bouton_modification.getText().toString().contains("Modification")) {
                        modification();
                    }
                    else if(bouton_modification.getText().toString().contains("Accepter"))
                    {
                        accepter();
                    }
                    break;
            }
        }
    };

    private void accepter() {
        bouton_modification.setText("Modification");
        C_user.get_instance().add_user(
                textView_nom.getText().toString(),
                textView_prenom.getText().toString());
        C_user.get_instance().get_user();
        textView_prenom.setText(C_user.get_instance().get_prenom_user());
        textView_nom.setText(C_user.get_instance().get_nom_user());
        textView_nom.setEnabled(false);
        textView_prenom.setEnabled(false);
    }

    private void modification() {
        bouton_modification.setText("Accepter");
        textView_nom.setEnabled(true);
        textView_prenom.setEnabled(true);
    }

    private void updateUIDisconnect() {
        bouton_create_account.setText("Connect");
    }


    public void connexion(){
        Intent connexionActivity = new Intent(getActivity(), Main2Activity.class);
        startActivity(connexionActivity);
    }

    public void deconnexion()
    {
        C_user.get_instance().get_mAuth().signOut();
        bouton_modification.setEnabled(false);
        textView_prenom.setText("");
        textView_nom.setText("");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        connexionViewModel =
                ViewModelProviders.of(this).get(ConnexionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_connexion, container, false);

        bouton_create_account = root.findViewById(R.id.create_account);
        bouton_modification = root.findViewById(R.id.bouton_modification);
        bouton_modification.setOnClickListener(mOnClickListener);
        bouton_create_account.setOnClickListener(mOnClickListener);
        textView_nom = root.findViewById(R.id.edit_nom);
        textView_prenom = root.findViewById(R.id.edit_prenom);
        textView_nom.setEnabled(false);
        textView_prenom.setEnabled(false);

        if(C_user.get_instance().get_currentUser() != null) {
            bouton_create_account.setText("Disconnect");
            Log.d("GET_USER", "DocumentSnapshot data: " + C_user.get_instance().get_prenom_user());
            textView_prenom.setText(C_user.get_instance().get_prenom_user());
            textView_nom.setText(C_user.get_instance().get_nom_user());
        }
        else{
            bouton_modification.setEnabled(false);
        }
        return root;
    }
}
