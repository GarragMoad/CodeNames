package controllers;

import java.sql.SQLException;

import dao.CarteDao;
import dao.PartieDao;
import dao.sendCarteRequestDao;
import models.sendCarteRequest;
import webserver.WebServerContext;

public class TourController {

    private static CarteDao carteDao;
    private static PartieDao partieDao;
    private static sendCarteRequestDao sendCarteRequestDao;

    public TourController() throws SQLException {
        carteDao=new CarteDao();
        sendCarteRequestDao= new sendCarteRequestDao();
        partieDao=new PartieDao();
    }

    public static void checkCarte(WebServerContext context) throws SQLException{
        sendCarteRequest bodyrequete= context.getRequest().extractBody(sendCarteRequest.class);
        System.out.println(bodyrequete.toString());
        String codePartie=bodyrequete.code();
        int posX=bodyrequete.posX();
        int posY=bodyrequete.posY();

        int idPartie=partieDao.getId(codePartie);
        int tourActuel=sendCarteRequestDao.getTour(idPartie);

        int nbMotsSusceptible=sendCarteRequestDao.getNbMotsSuscpetible(idPartie);
        int nbMotsTrouver=sendCarteRequestDao.getNbMotsTrouver(idPartie);
        int score = partieDao.getScore(idPartie);
        System.out.println("avant la condition");
        if(nbMotsTrouver<=nbMotsSusceptible+1){
            int couleurId=carteDao.getCouleurId(posX, posY, idPartie);
            System.out.println("dans le condition");
            switch (couleurId) {
                case 1:  // Le cas où la couleur est blue
                    if(nbMotsTrouver <= nbMotsSusceptible){  // si le mot choisi est inférieur à N+1
                        partieDao.updateScore(score+nbMotsTrouver, idPartie);
                    }
                    else if(nbMotsTrouver == nbMotsSusceptible+1){
                        partieDao.updateScore(score+(nbMotsTrouver*nbMotsTrouver), idPartie);
                    }
                    carteDao.updateEtatCarte(1, posX, posY, idPartie);
                    sendCarteRequestDao.updateNbMotsTrouver(nbMotsTrouver+1, idPartie);
                    context.getResponse().json("Bravo, vous avez trouvé un mot de couleur blue, votre score est de : " + partieDao.getScore(idPartie) );
                    break;
                case 2:  // Le cas où la couleur est grise
                    sendCarteRequestDao.updateTour(tourActuel+1, idPartie);
                    context.getResponse().json("Vous avez trouvé une case grise, votre tour est passé , votre score est de : " + score);
                    sendCarteRequestDao.updateNbMotsTrouver(nbMotsTrouver+1, idPartie);
                    carteDao.updateEtatCarte(1, posX, posY, idPartie);
                    break;
                case 3:  // Le cas où la couleur est noire
                    partieDao.updateEtatPartie(0, idPartie);
                    context.getResponse().json("OPS Carte noir Partie terminée: ");
                    carteDao.updateEtatCarte(1, posX, posY, idPartie);
                default:
                    break;
            }
            
    }
    else{
        context.getResponse().serverError("Tour terminé");
    }
}
}
