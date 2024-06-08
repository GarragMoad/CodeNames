package controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.CarteDao;
import models.Carte;
import webserver.WebServerContext;

public class CarteController {

    public CarteController() {
    }

    public static ArrayList<Carte> getGrille(WebServerContext context , String codePartie) throws SQLException {
         CarteDao carteDao = new CarteDao();
         ArrayList<Carte>cartes=carteDao.getGrille(codePartie);
         if(cartes.size()==0){
             throw new SQLException("Erreur dans la fonction getGrille() de CarteController.java : la grille n'a pas été trouvée");
         }
         else{
            //context.getResponse().json(cartes);
            System.out.println("Grille récupérée avec succès");
         }
         return cartes;
    }

    public ArrayList<Carte> getGrilleFromPartie(String codePartie) throws SQLException {
        CarteDao carteDao = new CarteDao();
        ArrayList<Carte>cartes=carteDao.getGrille(codePartie);
        if(cartes.size()==0){
            throw new SQLException("Erreur dans la fonction getGrilleFromPartie() de CarteController.java : la grille n'a pas été trouvée");
        }
        else{
            System.out.println("Grille récupérée avec succès");
        }
        return cartes;
    }
        
    




}
