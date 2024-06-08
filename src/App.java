import java.sql.SQLException;

import controllers.CarteController;
import controllers.PartieController;
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

        webserver.getRouter().post("/CreatePartie", (WebServerContext context)->{
            try {
             PartieController.CreatePartie(context);
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("erreur dans la rquête post de la route /CreatePartie");
         }
         });

         webserver.getRouter().post("/JoinPartie", (WebServerContext context)->{
            try {
             PartieController.JoinPartie(context);
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("erreur dans la rquête post de la route /JoinPartie");
         }
         });


    }
    
}
