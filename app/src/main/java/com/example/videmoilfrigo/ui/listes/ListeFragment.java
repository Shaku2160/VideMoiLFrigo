package com.example.videmoilfrigo.ui.listes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.videmoilfrigo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListeFragment extends Fragment {

    public ArrayList<String> list = new ArrayList<>();

    private ListeViewModel listeViewModel;




   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putStringArrayList("Liste", list);

        // etc.
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        listeViewModel =
                ViewModelProviders.of(this).get(ListeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_liste, container, false);


      /*  if	(savedInstanceState	!=	null)
        {
            list = savedInstanceState.getStringArrayList("Liste");

        }*/
        return root;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        final Activity me = getActivity();

        final ListView liste = getView().findViewById(R.id.ListeCourse);
        final TextAdapter adapter = new TextAdapter();

        // les informations sauvegardé sont lu
        readInfo("savedList");
        adapter.setData(list);
        liste.setAdapter(adapter);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Voulez-vous supprimez ce produit ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list);
                                saveInfo("savedList");

                            }
                        })
                        .setNegativeButton("Non", null)
                        .create();
                dialog.show();
            }
        });

        final Button newProductButton = getView().findViewById(R.id.newProductButton);

        newProductButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                // récupère la liste de tout les ingredients dans le fichier Strings.xml
                ArrayList<String> ingredients = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.ingredients)));

                ArrayAdapter<String> adapterAutoFill = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, ingredients);

                final AutoCompleteTextView ingredientsFields = new AutoCompleteTextView(me);
                ingredientsFields.setAdapter(adapterAutoFill);


                //productInput.setSingleLine();
                ingredientsFields.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder( me)
                        .setTitle("Ajout d'un nouveau produit")
                        .setMessage("Que voulez-vous ajouter ?")
                        .setView(ingredientsFields)
                        .setPositiveButton("Ajout Produit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(ingredientsFields.getText().toString());

                                adapter.setData(list);
                                // myRef.setValue(productInput.getText().toString());


                                saveInfo("savedList");
                            }
                        })
                        .setNegativeButton("Annuler",null)
                        .create();

                dialog.show();
                ingredientsFields.requestFocus();
            }
        });

        final Button clearListeButton = getView().findViewById(R.id.clearListeButton);

        clearListeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){



                AlertDialog dialog = new AlertDialog.Builder( me)
                        .setTitle("Suppression de toute la liste")
                        .setMessage("Voulez vous ajouter la liste à votre frigo ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                            // TODO Ajouter au frigo
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                saveInfo("newFrigo");
                                list.clear();
                                adapter.setData(list);
                                saveInfo("savedList");

                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog dialogNon = new AlertDialog.Builder(me)
                                        .setTitle("ATTENTION : Suppression définitive")
                                        .setMessage("Êtes vous sûr de supprimez toutes la liste ?")
                                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                list.clear();
                                                adapter.setData(list);
                                                saveInfo("savedList");
                                            }
                                        })
                                        .setNegativeButton("Non", null)
                                        .create();

                                dialogNon.show();
                            }
                        })
                        .create();

                dialog.show();
            }
        });


    }

    private void saveInfo(String titre){
        try {
            File file = new File(getContext().getFilesDir(),titre);

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            for(int i = 0; i<list.size(); i++){
                bw.write(list.get(i));
                bw.newLine();
            }

            bw.close();
            fOut.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readInfo(String titre){
        File file = new File(getContext().getFilesDir(),titre);
        if(!file.exists()){
            return;
        }

        try{

            FileInputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader( new InputStreamReader(is));

            String line = br.readLine();

            while(line != null){
                list.add(line);
                line = br.readLine();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // chaque fois que l'appli est en pause, les informations sont sauvegardé
    //@Override
    //public void onPause(){
    //  super.onPause();
    // savedInfo();
    //}

    class TextAdapter extends BaseAdapter {

        List<String> list = new ArrayList<>();

        void setData(List<String> mList){
            list.clear();

            list.addAll(mList);
            notifyDataSetChanged();
        }



        @Override
        public int getCount(){

            return list.size();
        }

        @Override
        public Object getItem(int position){

            return null;
        }


        @Override
        public long getItemId(int position){

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if(convertView == null){
                LayoutInflater inflater =(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.item, parent, false);

            }

            TextView textView = convertView.findViewById(R.id.produit);

            textView.setText(list.get(position));

            return convertView;

        }
    }
}