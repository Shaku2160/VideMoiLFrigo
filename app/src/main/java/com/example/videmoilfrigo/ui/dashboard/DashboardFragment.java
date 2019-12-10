package com.example.videmoilfrigo.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videmoilfrigo.DetailRecette;
import com.example.videmoilfrigo.R;
import com.example.videmoilfrigo.Recette;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Recette> listeRecette = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);



        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final Button APIButton = getView().findViewById(R.id.buttonAPI);
        final RecyclerView liste = getView().findViewById(R.id.ListeRecettes);
        final EditText nbRecette = getView().findViewById(R.id.nbRecette);


        // mise en place de l'adapter pour la recycleView des recettes
        final Recettes adapter = new Recettes(listeRecette);

        liste.setAdapter(adapter);
        liste.setLayoutManager(new LinearLayoutManager(getContext()));


        APIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readIngredient();

                Log.d("read ingred >>>>>>>",list.toString() );

                String ingredients = "water";
                for (int i = 0; i < list.size(); i++) {
                    ingredients +=",+"+list.get(i);
                }

                // configuration de l'url pour l'API
                // Recherche des recettes par ingredients
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="https://api.spoonacular.com/recipes/findByIngredients?" +
                        "ingredients="+ingredients+"&number="+nbRecette.getText()+
                        "&apiKey=5af23d9e1f1d478b8f353dd0cb64ac8c";


                // Configuration de la requete URL
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("test API >>>>>>>>>>>", response);
                                try {
                                    // récupération de la réponse sous format JSON
                                    JSONArray jArray = new JSONArray(response);

                                    if(listeRecette.size() > 0) {
                                        listeRecette.clear();
                                        adapter.notifyDataSetChanged();
                                    }

                                    // on parse le JSONArray pour en extraire les informations
                                    for(int i=0; i<jArray.length();i++){

                                        JSONObject oneObject = jArray.getJSONObject(i);
                                        String titre = oneObject.getString("title");
                                        String image = oneObject.getString("image");
                                        String id = oneObject.getString("id");

                                        Recette recette = new Recette(titre, image, id);

                                        listeRecette.add(recette);

                                        adapter.notifyItemInserted(0);

                                        Log.d("test parser >>>>>>>>>>>", titre +" "+ image );
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

                // Ajoute la requete à la file
                queue.add(stringRequest);
            }
        });
    }

    // lecture du fichier local "savedFrigo" pour récupérer les ingredients disponible
    private void readIngredient() {
        File file = new File(getContext().getFilesDir(), "savedFrigo");
        if (!file.exists()) {
            return;
        }

        try {

            FileInputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();

            while (line != null) {
                list.add(line);
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Adapter de la RecycleView
    class Recettes extends RecyclerView.Adapter<Recettes.ViewHolder> {

        ArrayList<Recette> mRecettes;




        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView titreRecette;
            public ImageView imageRecette;


            public ViewHolder(View itemView) {

                super(itemView);

                titreRecette = itemView.findViewById(R.id.titreRecette);
                imageRecette = itemView.findViewById(R.id.imageRecette);

                // ajoute un listener sur chaque item de la recycleView
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Recette recette = mRecettes.get(position);

                    Toast.makeText(getContext(), titreRecette.getText(), Toast.LENGTH_SHORT).show();



                    Intent intent = new Intent(getActivity(), DetailRecette.class);

                    intent.putExtra("rTitre",recette.getTitre());
                    intent.putExtra("rImage",recette.getImage());
                    intent.putExtra("rId",recette.getId());
                    startActivity(intent);
                }

            }

        }

        public Recettes(ArrayList<Recette> recettes) {
            mRecettes = recettes;
        }

        @NonNull
        @Override
        public Recettes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.recettes, parent, false);

            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Recettes.ViewHolder holder, int position) {
            Recette recette = mRecettes.get(position);

            TextView titre = holder.titreRecette;
            ImageView image = holder.imageRecette;

            titre.setText(recette.getTitre());


            // utilisation de Picasso pour charger une image grace à son URL
            Picasso.get().load(recette.getImage()).into(image);




        }



        @Override
        public int getItemCount() {
            return mRecettes.size();
        }

    }
}

