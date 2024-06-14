package controllers;

import java.sql.SQLException;

import dao.CarteDao;
import dao.IndicerequestDao;
import dao.JoueurDao;
import dao.JoueurPartieDao;
import dao.MotDao;
import dao.PartieDao;
import dao.sendCarteRequestDao;
import models.Carte;
import models.sendCarteRequest;
import webserver.WebServerContext;

public class CheckMotController {
    
    private static CarteDao carteDao ;
    private static PartieDao partieDao;
    private static sendCarteRequestDao sendCarteRequestDao;
    private static IndicerequestDao indiceRequestDao;
    private static MotDao  motDao;

    static {
        try {
            partieDao = new PartieDao();
            carteDao=new CarteDao();
            sendCarteRequestDao= new sendCarteRequestDao();
            indiceRequestDao=new IndicerequestDao();
            motDao=new MotDao();
        } catch (SQLException e) {
            System.out.println("Erreur dans le bloc static de PartieController.java");
        }
    }


    public CheckMotController(){
    }

    public static void checkCarte(WebServerContext context) throws SQLException{
        sendCarteRequest bodyrequete= context.getRequest().extractBody(sendCarteRequest.class);
        String codePartie=bodyrequete.code();
        int posX=bodyrequete.posX();
        int posY=bodyrequete.posY();
    
        int idPartie=partieDao.getId(codePartie);
        System.out.println("idPartie : "+idPartie);
         
        int tourActuel=indiceRequestDao.getTour(idPartie);
        System.out.println("tour actuel : "+tourActuel);

        int nbMotsSusceptible=sendCarteRequestDao.getNbMotsSuscpetible(idPartie);
        System.out.println("nbMotsSusceptible : "+nbMotsSusceptible);
        int nbMotsTrouver=sendCarteRequestDao.getNbMotsTrouver(idPartie);
        System.out.println("nbMotsTrouver : "+nbMotsTrouver);
        int score = partieDao.getScore(idPartie);
        System.out.println("score : "+score);
        int couleurId=carteDao.getCouleurId(posX, posY, idPartie);
        System.out.println("couleurId : "+couleurId);

        if(nbMotsTrouver<=nbMotsSusceptible+1){
            switch (couleurId) {
                case 1:  // Le cas où la couleur est blue
                        int incrtNbMot=nbMotsTrouver++;
                        sendCarteRequestDao.updateNbMotsTrouver(incrtNbMot, idPartie);
                        nbMotsTrouver=sendCarteRequestDao.getNbMotsTrouver(idPartie);
                        if(nbMotsTrouver <= nbMotsSusceptible){  // si le mot choisi est inférieur à N+1
                            partieDao.updateScore(score+nbMotsTrouver, idPartie);
                            System.out.println("score : "+partieDao.getScore(idPartie));
                        }
                        else if(nbMotsTrouver == nbMotsSusceptible+1){
                            partieDao.updateScore(score+(nbMotsTrouver*nbMotsTrouver), idPartie);
                        }
                        carteDao.updateEtatCarte(1, posX, posY, idPartie);
                        sendCarteRequestDao.updateNbMotsTrouver(nbMotsTrouver+1, idPartie);
                        context.getSSE().emit("score",partieDao.getScore(idPartie) );
                        // context.getResponse().json("Bravo, vous avez trouvé un mot de couleur blue, votre score est de : " + partieDao.getScore(idPartie) );
                        context.getResponse().json(score);
                        break;
                case 2:  // Le cas où la couleur est grise
                    sendCarteRequestDao.updateTour(tourActuel+1, idPartie);
                    context.getSSE().emit("score",partieDao.getScore(idPartie) );
                    context.getResponse().json(score);
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
        context.getResponse().serverError("Vous avez dépassé le nombre de mots susceptible de trouver, votre score est de : " + score + "tour terminé");
    }
}

}
