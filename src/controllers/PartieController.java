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
import models.JoinPartieBody;
import dao.CarteDao;
import dao.JoueurDao;
import dao.JoueurPartieDao;
import webserver.WebServerContext;

public class PartieController {

    private static PartieDao partieDao;
    private static JoueurPartieDao joueurPartieDao;
    private static JoueurDao joueurDao;
    private static CarteDao carteDao;
    
    static {
        try {
            partieDao = new PartieDao();
            joueurDao=new JoueurDao();
            joueurPartieDao=new JoueurPartieDao();
            carteDao=new CarteDao();
        } catch (SQLException e) {
            System.out.println("Erreur dans le bloc static de PartieController.java");
        }
    }

    public PartieController(){
    }

    public static void CreatePartie(WebServerContext context) throws SQLException{

        System.out.println("Création de la partie");
        CreatePartieBody bodyrequete= context.getRequest().extractBody(CreatePartieBody.class);
        System.out.println(bodyrequete.toString());
        String nom=bodyrequete.nom();
        int role=bodyrequete.role();
        System.out.println(nom+" "+role);
       
        int idJoueur=joueurDao.CreateJoueur(nom,role);
        if(idJoueur==-1){
            context.getResponse().serverError("Le nom est déjà utilisé");
        }
        else{
            String code=partieDao.CreatePartie();
            int idPartie=partieDao.getId(code);
            
            
            String roleOut=joueurDao.getRole(idJoueur);
   
           joueurPartieDao.InsertJoueurPartie(idJoueur, idPartie);
   
           ArrayList<Carte>cartes = CarteController.getGrille(context,code);
           JsonObject combinedObject = responseJson(code, roleOut, cartes);
    
            context.getSSE().emit("codePartie", code);
            context.getResponse().json(combinedObject); 
        }
        
    }

    public static void JoinPartie(WebServerContext context) throws SQLException{
        int idJoueur=-1;
        JoinPartieBody bodyrequete= context.getRequest().extractBody(JoinPartieBody.class);
        String nom=bodyrequete.nom();
        String code=bodyrequete.code();
    
        int idPartie=partieDao.getId(code);

        //On vérifie si la partie est toujours en cours
        if(partieDao.getEtatPartie(idPartie)==1){   
            int idAutreJoeur=joueurPartieDao.getIdJoueur(idPartie);
            String roleAutreJoueur= joueurDao.getRole(idAutreJoeur);
    
            if(roleAutreJoueur.equals("Maitre de mots")){
                    idJoueur=joueurDao.CreateJoueur(nom,2);     
                }
            else if(roleAutreJoueur.equals("Maitre d intuition")){
                    idJoueur=joueurDao.CreateJoueur(nom,1);
                }
                //
            if(idJoueur==-1){
                    context.getResponse().serverError("Le nom est déjà utilisé");
                }
            else{
                    String roleOut=joueurDao.getRole(idJoueur);
                    joueurPartieDao.InsertJoueurPartie(idJoueur, idPartie);
            
                    ArrayList<Carte>cartes = carteDao.getGrillePartie(code);
            
                    JsonObject combinedObject = responseJson(code, roleOut, cartes);
            
                    context.getResponse().json(combinedObject);
                }
            }
            else{
                context.getResponse().serverError("La partie que vous demandez est terminée");
            }
           
    }
       

    //On va combiner le code de la partie et la grille pour qu'on les envoie dans une seule réponse
    private static JsonObject responseJson(String code, String role, ArrayList<Carte> cartes){
        Gson gson = new GsonBuilder().create();
        JsonObject combinedObject = new JsonObject();
        combinedObject.addProperty("code", code);
        combinedObject.addProperty("role", role);
        JsonArray jsonArray = new JsonArray();
        for (Carte carte : cartes) {
            jsonArray.add(gson.toJsonTree(carte));
        }
        combinedObject.add("cartes", jsonArray);
        return combinedObject;

    }
}
