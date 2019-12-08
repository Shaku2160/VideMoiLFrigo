package com.example.videmoilfrigo;

public class Recette {

        private String titre;
        private String mImage;
        private String mId;
        //private ArrayList<String> ingredients;

        public Recette(String title, String image, String id){
            titre = title;
            mImage = image;
            mId = id;
            //ingredients = ingredients; ArrayList<String> ingredients
        }

        public String getTitre(){
            return this.titre;
        }

        public String getImage(){
            return this.mImage;
        }

        public String getId(){
        return this.mId;
    }


}
