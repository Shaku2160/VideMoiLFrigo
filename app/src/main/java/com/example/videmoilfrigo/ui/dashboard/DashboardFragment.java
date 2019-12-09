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
    public ArrayList<String> list = new ArrayList<>();
    public ArrayList<Recette> listeRecette = new ArrayList<>();

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

                // CALL API
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="https://api.spoonacular.com/recipes/findByIngredients?" +
                        "ingredients="+ingredients+"&number="+nbRecette.getText()+
                        "&apiKey=5af23d9e1f1d478b8f353dd0cb64ac8c";


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

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }

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



    class Recettes extends RecyclerView.Adapter<Recettes.ViewHolder> {

        ArrayList<Recette> mRecettes;




        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            // Your holder should contain a member variable
            // for any view that will be set as you render a row

            public TextView titreRecette;
            public ImageView imageRecette;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {

                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                titreRecette = itemView.findViewById(R.id.titreRecette);
                imageRecette = itemView.findViewById(R.id.imageRecette);

                // Attach a click listener to the entire row view
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                int position = getAdapterPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    Recette recette = mRecettes.get(position);
                    // We can access the data within the views
                    Toast.makeText(getContext(), titreRecette.getText(), Toast.LENGTH_SHORT).show();

//                    Fragment fragment = null;
//
//                    fragment = new ListeFragment();
//                    replaceFragment(fragment);

                    Intent intent = new Intent(getActivity(), DetailRecette.class);

                    intent.putExtra("rTitre",recette.getTitre());
                    intent.putExtra("rImage",recette.getImage());
                    intent.putExtra("rId",recette.getId());
                    startActivity(intent);
                }

            }

//            public void replaceFragment(Fragment someFragment) {
//                Log.d("CLICK !!!!!!!!!!!!!!", "ez");
//                Toast.makeText(getContext(), "CLICK !", Toast.LENGTH_SHORT).show();
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                transaction.replace(R.id.navigation_liste, someFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
        }

        // Pass in the contact array into the constructor
        public Recettes(ArrayList<Recette> recettes) {
            mRecettes = recettes;
        }

        @NonNull
        @Override
        public Recettes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.recettes, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Recettes.ViewHolder holder, int position) {
            // Get the data model based on position
            Recette recette = mRecettes.get(position);

            // Set item views based on your views and data model
            TextView titre = holder.titreRecette;
            ImageView image = holder.imageRecette;

            titre.setText(recette.getTitre());


            // Log.d("IMAGE ->>>>>> ",recette.getImage());
            Picasso.get().load(recette.getImage()).into(image);




        }



        @Override
        public int getItemCount() {
            return mRecettes.size();
        }

    }
}

