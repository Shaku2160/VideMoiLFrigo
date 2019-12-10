package com.example.videmoilfrigo.ui.home;

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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.videmoilfrigo.C_user;
import com.example.videmoilfrigo.FireBaseDataTools;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;




    private ArrayList<String> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);



        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        final Activity me = getActivity();

        final ListView liste = getView().findViewById(R.id.ListeFrigo);
        final HomeFragment.TextAdapter adapter = new HomeFragment.TextAdapter();

        // les informations sauvegardé sont lu
        addNewProductFromList();
        readInfo();
        adapter.setData(list);
        liste.setAdapter(adapter);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Voulez-vous supprimez ce produit ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);

                                if(C_user.get_instance().get_currentUser() != null) {
                                    Map<String, Object> data = new HashMap<>();
                                    for (String s: list) {
                                        data.put(s, 1);
                                    }
                                    FireBaseDataTools.get_instance().add_map_in_document("frigo",
                                            C_user.get_instance().get_currentUser().getUid(),
                                            data);
                                }

                                adapter.setData(list);
                                saveInfo();

                            }
                        })
                        .setNegativeButton("Non", null)
                        .create();
                dialog.show();
            }
        });

        final Button newProductButton = getView().findViewById(R.id.newProductFrigoButton);





        newProductButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // récupère la liste de tout les ingredients dans le fichier Strings.xml
                ArrayList<String> ingredients = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.ingredients)));

                ArrayAdapter<String> adapterAutoFill = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, ingredients);

                final AutoCompleteTextView ingredientsFields = new AutoCompleteTextView(me);
                ingredientsFields.setAdapter(adapterAutoFill);



                ingredientsFields.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(me)
                        .setTitle("Ajout d'un nouveau produit")
                        .setMessage("Que voulez-vous ajouter ?")
                        .setView(ingredientsFields)
                        .setPositiveButton("Ajout Produit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(ingredientsFields.getText().toString());

                                if(C_user.get_instance().get_currentUser() != null) {
                                    Map<String, Object> data = new HashMap<>();
                                    for (String s: list) {
                                        data.put(s, 1);
                                    }
                                    FireBaseDataTools.get_instance().add_map_in_document("frigo",
                                            C_user.get_instance().get_currentUser().getUid(),
                                            data);
                                }

                                adapter.setData(list);

                                saveInfo();
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();

                dialog.show();
                ingredientsFields.requestFocus();
            }
        });

        final Button clearListeButton = getView().findViewById(R.id.clearFrigoButton);

        clearListeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                AlertDialog dialogNon = new AlertDialog.Builder(me)
                        .setTitle("ATTENTION : Suppression définitive de votre frigo")
                        .setMessage("Êtes vous sûr de supprimez toutes les produits ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.clear();

                                if(C_user.get_instance().get_currentUser() != null) {
                                    Map<String, Object> data = new HashMap<>();
                                    for (String s: list) {
                                        data.put(s, 1);
                                    }
                                    FireBaseDataTools.get_instance().add_map_in_document("frigo",
                                            C_user.get_instance().get_currentUser().getUid(),
                                            data);
                                }

                                adapter.setData(list);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Non", null)
                        .create();

                dialogNon.show();
            }


        });


    }

    private void saveInfo() {
        try {
            File file = new File(getContext().getFilesDir(), "savedFrigo");

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i));
                bw.newLine();
            }

            bw.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readInfo() {
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

    private void addNewProductFromList(){
        File fileNew = new File(getContext().getFilesDir(), "newFrigo");
        File fileFrigo = new File(getContext().getFilesDir(), "savedFrigo");

        if (!fileNew.exists()) {
            return;
        }

        try {

            FileInputStream is = new FileInputStream(fileNew);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            FileOutputStream fOut = new FileOutputStream(fileFrigo, true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            String line = br.readLine();

            ArrayList<String> tmp = new ArrayList<String>();

            while (line != null) {
                tmp.add(line);
                line = br.readLine();
            }

            for (int i = 0; i < tmp.size(); i++) {
                bw.write(tmp.get(i));
                bw.newLine();
            }

            bw.close();
            fOut.close();


            fileNew.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // adapter de la textView
    class TextAdapter extends BaseAdapter {

        List<String> list = new ArrayList<>();

        void setData(List<String> mList) {
            list.clear();

            list.addAll(mList);
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }


        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.item, parent, false);

            }

            TextView textView = convertView.findViewById(R.id.produit);

            textView.setText(list.get(position));

            return convertView;

        }
    }
}