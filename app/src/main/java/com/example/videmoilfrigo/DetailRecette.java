package com.example.videmoilfrigo;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailRecette extends AppCompatActivity{


    ArrayList<String> etapes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_liste)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        ImageView vImage = this.findViewById(R.id.imageRecetteDetail);
        TextView vTitre = this.findViewById(R.id.titreRecetteDetail);
        final ListView listEtape = findViewById(R.id.listEtape);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String titre = extras.getString("rTitre");

            String image = extras.getString("rImage");
            String id = extras.getString("rId");

            vTitre.setText(titre);
            Picasso.get().load(image).into(vImage);


            //The key argument here must match that used in the other activity
            // CALL API
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://api.spoonacular.com/recipes/"+id+"/analyzedInstructions"+
                    "?apiKey=5af23d9e1f1d478b8f353dd0cb64ac8c";


            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("test API >>>>>>>>>>>", response);
                            try {
                                JSONArray jArray = new JSONArray(response);

                                for(int i=0; i<jArray.length();i++){

                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    // Pulling items from the array

                                    JSONArray steps = oneObject.getJSONArray("steps");

                                    etapes.clear();

                                    String[] test = new String[steps.length()];
                                    for(int y=0;y<steps.length();y++){

                                        JSONObject json=(JSONObject) steps.get(y);

                                        etapes.add(json.getString("step"));
                                        test[y] = json.getString("step");

                                    }


                                    for (String etape: etapes) {
                                        Log.d("steps in >>>>>>>>>>>",  etape);
//

                                    }

                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailRecette.this,
                                            android.R.layout.simple_list_item_1, android.R.id.text1, test); // You have set     the previous array to an adapter that can be now setted to a list.
//
//                                        final ArrayAdapter<String> adaptegyjr = new ArrayAdapter<>()
//                                        listView.setAdapter(adapter); //Set adapter and that's it.

//                                        final StableArrayAdapter adapter = new StableArrayAdapter(getParent(),
//                                                android.R.layout.simple_list_item_1, etapes);
                                    listEtape.setAdapter(adapter);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("test API >>>>>>>>>>>", error.toString());
                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

            for (String etape: etapes) {
                Log.d("steps out >>>>>>>>>>>",  etape);
            }

        }



    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
    @Override
    public void onStart() {
        super.onStart();


    }






}
