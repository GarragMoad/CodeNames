package controllers;

import java.sql.SQLException;

import javax.swing.plaf.metal.MetalBorders.ScrollPaneBorder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import dao.CarteDao;
import dao.IndicerequestDao;
import dao.JoueurDao;
import dao.JoueurPartieDao;
import dao.PartieDao;
import models.CreatePartieBody;
import models.IndiceRequest;
import webserver.WebServer;
import webserver.WebServerContext;

public class IndiceRequestController {
    private static IndicerequestDao indiceRequestDao;
    private static  PartieDao partieDao  ;
    private static int tourActuel;

     static {
        try {
            partieDao = new PartieDao();
            indiceRequestDao=new IndicerequestDao();
            
        } catch (SQLException e) {
            System.out.println("Erreur dans le bloc static de PartieController.java");
        }
    }

    public static void insertIndice(WebServerContext context) throws SQLException{
        IndiceRequest bodyrequete= context.getRequest().extractBody(IndiceRequest.class);
        System.out.println(bodyrequete.toString());
        String code=bodyrequete.code();
        int nbMotsSusceptible=bodyrequete.nbMotsSusceptible();
        String indice=bodyrequete.indice();

        int idPartie=partieDao.getId(code);
        tourActuel=indiceRequestDao.getTour(idPartie);
        IndiceRequestController.tourActuel++;
        indiceRequestDao.insertIndice(indice, nbMotsSusceptible, idPartie,IndiceRequestController.tourActuel);
        //context.getSSE().emit("indice", bodyrequete);
        JsonObject combinedObject = new JsonObject();
        combinedObject.addProperty("indice", indice);
        combinedObject.addProperty("nbMotsSusceptible", nbMotsSusceptible);
        context.getResponse().json(combinedObject);
        
    }


}
