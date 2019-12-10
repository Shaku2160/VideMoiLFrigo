package com.example.videmoilfrigo;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


public class DetailRecette extends AppCompatActivity{


    ArrayList<String> etapes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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


            // appel de l'api pour récupérer les détail de la recette par son ID
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://api.spoonacular.com/recipes/"+id+"/analyzedInstructions"+
                    "?apiKey=5af23d9e1f1d478b8f353dd0cb64ac8c";


            // Configuration de la requete URL
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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
                                            android.R.layout.simple_list_item_1, android.R.id.text1, test);
//
//
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

            // ajoute la requete à la file
            queue.add(stringRequest);

            for (String etape: etapes) {
                Log.d("steps out >>>>>>>>>>>",  etape);
            }

        }



    }


    @Override
    public void onStart() {
        super.onStart();


    }






}
