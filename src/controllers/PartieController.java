package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.PartieDao;
import models.Carte;
import models.CreatePartieBody;
import dao.JoueurDao;
import dao.JoueurPartieDao;
import webserver.WebServerContext;

public class PartieController {

    private static PartieDao partieDao;
    private static JoueurPartieDao joueurPartieDao;
    private static JoueurDao joueurDao;
    
    static {
        try {
            partieDao = new PartieDao();
            joueurDao=new JoueurDao();
            joueurPartieDao=new JoueurPartieDao();
        } catch (SQLException e) {
            System.out.println("Erreur dans le bloc static de PartieController.java");
        }
    }

    public PartieController(){
    }

    public static void CreatePartie(WebServerContext context) throws SQLException{
        CreatePartieBody bodyrequete= context.getRequest().extractBody(CreatePartieBody.class);
        System.out.println(bodyrequete.toString());
        String nom=bodyrequete.nom();
        int role=bodyrequete.role();
        
         String code=partieDao.CreatePartie();
         int idPartie=partieDao.getId(code);
         int idJoueur=joueurDao.CreateJoueur(nom,role);

        joueurPartieDao.InsertJoueurPartie(idJoueur, idPartie);

        ArrayList<Carte>cartes = CarteController.getGrille(context,code);

        //On va combiner le code de la partie et la grille pour qu'on les envoie dans une seule réponse
        Gson gson = new GsonBuilder().create();
        JsonObject combinedObject = new JsonObject();
        combinedObject.addProperty("code", code);
        JsonArray jsonArray = new JsonArray();
        for (Carte carte : cartes) {
            jsonArray.add(gson.toJsonTree(carte));
        }
        combinedObject.add("cartes", jsonArray);

        context.getResponse().json(combinedObject);

        // context.getSSE().emit("essai", joueurEquipePartie);
        // System.out.println("Partie créée avec succès");
       
    }
}
