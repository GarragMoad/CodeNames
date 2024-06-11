import java.sql.SQLException;

import controllers.CarteController;
import controllers.IndiceRequestController;
import controllers.PartieController;
import controllers.TourController;
import dao.CarteDao;
import models.Carte;
import models.Couleur;
import webserver.WebServer;
import webserver.WebServerContext;
import webserver.WebServerSSEEventType;

public class App {
    public static void main(String[] args) throws Exception {
        WebServer webserver = new WebServer();
        webserver.listen(8081);

        // webserver.getRouter().get("/getGrille", (WebServerContext context)->{
        //     try {
        //         CarteController.getGrille(context,"XXXXXX");
        //     } catch (SQLException e) {
        //         e.printStackTrace();
        //         System.out.println("erreur dans la rquête get de la route /getGrille");
        //     }
        // });

        //{IN : {nom:"toto",role="1"}}
        //{OUT : {code :"XXXXXX" , role:"Maitre de mots"}, cartes {list}}
        webserver.getRouter().post("/CreatePartie", (WebServerContext context)->{
            try {
             PartieController.CreatePartie(context);
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("erreur dans la rquête post de la route /CreatePartie");
         }
         });

         //{IN : {nom:"titi",code="XXXXXX"}}
        //{OUT : {code :"XXXXXX" , role:"Maitre d intuition", cartes {list}}
         webserver.getRouter().post("/JoinPartie", (WebServerContext context)->{
            try {
             PartieController.JoinPartie(context);
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("erreur dans la rquête post de la route /JoinPartie");
         }
         });
         
         webserver.getRouter().post("/CheckMot", (WebServerContext context)->{
            try {
                TourController.checkCarte(context);
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("erreur dans la rquête post de la route /CheckMot");
         }
         });

         //IN{indice:"mot",nbMotsSusceptible:3 , code="XXXXX"}
        //OUT{indice:"mot" , NbmotsSusceptible:3}
         webserver.getRouter().post("/indice", (WebServerContext context)->{
            try {
                IndiceRequestController.insertIndice(context);
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("erreur dans la rquête post de la route /CheckMot");
         }
         });


    }
    
}
