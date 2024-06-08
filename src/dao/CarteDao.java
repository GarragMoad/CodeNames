package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import database.polynamesDatabase;
import models.Carte;
import models.Couleur;
import models.Mot;
import models.Partie;

public class CarteDao {
    private final polynamesDatabase database;
    private final PartieDao partieDao;
    private final MotDao motDao;
    private final indiceDao indiceDao;

    public CarteDao() throws SQLException {
        this.database = new polynamesDatabase();
        this.partieDao= new PartieDao();
        this.motDao= new MotDao();
        this.indiceDao= new indiceDao();
    }

        public ArrayList<Carte> findAll() {
            ArrayList<Carte> cartes = new ArrayList<Carte>();
            try {
                
                String query = "SELECT * FROM carte";
                PreparedStatement statement = this.database.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int foundId = resultSet.getInt("id");
                    int positionX = resultSet.getInt("posX");
                    int positionY = resultSet.getInt("posY");
                    boolean etat_carte = resultSet.getBoolean("isCheck");
                    int id_Partie = resultSet.getInt("idPartie");
                    int idMot = resultSet.getInt("idMot");
                    int id_couleur = resultSet.getInt("idCouleur");
                    int idIncide = resultSet.getInt("idIndice");

                    String code_partie=this.partieDao.getCodePartie(id_Partie);
                    String mot=this.motDao.getMot(idMot);
                    String indice=this.indiceDao.getIndice(idIncide);

                    cartes.add(new Carte(foundId,positionX,positionY,etat_carte,code_partie,mot,Couleur.getByIndex(id_couleur-1),indice));
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("erreur dans la fonction findAll() de MotDao.java");
            }
            return cartes;
        }

        //fonction qui retourne une carte sachant sa position dans la grille et le code de la partie
        public Carte getCarte(int idPartie, int posX,int posY){
            try {
                String query = "SELECT * FROM carte WHERE idPartie = ? AND posX = ? AND posY = ?";
                PreparedStatement statement = this.database.prepareStatement(query);
                statement.setInt(1, idPartie);
                statement.setInt(2, posX);
                statement.setInt(3, posY);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int foundId = resultSet.getInt("id");
                    boolean etat_carte = resultSet.getBoolean("isCheck");
                    int idMot = resultSet.getInt("idMot");
                    int id_couleur = resultSet.getInt("idCouleur");
                    int idIncide = resultSet.getInt("idIndice");

                    String code_partie=this.partieDao.getCodePartie(idPartie);
                    String mot=this.motDao.getMot(idMot);
                    String indice=this.indiceDao.getIndice(idIncide);
                    System.out.println("Mot trouvé pour la partie  : "+code_partie+" : "+ mot  + "Fonction getCarte() de CarteDao.java");

                    return new Carte(foundId,posX,posY,etat_carte,code_partie,mot,Couleur.getByIndex(id_couleur-1),indice);
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("erreur dans la fonction getCarte() de CarteDao.java");
            }
            return null;
        }

        public ArrayList<Carte> getGrille(String code_partie){
            ArrayList <Carte> grille = new ArrayList<Carte>();
            List<String> mots = this.motDao.getGrille();  // on récupère 25 mots aléatoires pour construire la grille

                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        int couleurRabdom=  getRandomInteger();   // fonction qui attribue les couleurs aléatoirement aux cartes de la partie
                        int idPartie = this.partieDao.getId(code_partie);
                        int idMot = this.motDao.getId(mots.get(i*5+j));
                        //System.out.println("Mot trouvé pour la partie  : "+code_partie+" : "+ mots.get(i*5+j)  + "Fonction getCarte() de CarteDao.java");
                        try {
                            String insertQuery = "INSERT INTO carte (posX, posY, isCheck, idPartie, idMot, idCouleur) VALUES (?, ?, ?, ?, ?, ?)";
                            PreparedStatement insertStatement = this.database.prepareStatement(insertQuery);
                            insertStatement.setInt(1, i);
                            insertStatement.setInt(2, j);
                            insertStatement.setBoolean(3, false);
                            insertStatement.setInt(4, idPartie);
                            insertStatement.setInt(5, idMot);
                            insertStatement.setInt(6, couleurRabdom+1);    // On sait que notre BDD est construite de sorte que les couleurs sont indexées de 1 à 3 (BLUE , GRIS , NOIR)
                            insertStatement.executeUpdate();
                            insertStatement.close();
                        } catch (Exception e) {
                            System.out.println("erreur dans la fonction getGrille() de CarteDao.java lors de l'insertion de la carte dans la base de données");
                        }
                        
                        //Si tout se passe bien lors de l'insertion on récupère cette carte afin de construire la grille
                        
                        grille.add(getCarte(idPartie, i, j));                    
                    }
                }
                
            
            return grille;
        }

        //fonction qui attribue les couleurs aléatoirement aux cartes de la partie
        private int getRandomInteger(){
            List<Integer> weightedList = new ArrayList<>();
            for (int i = 0; i < 15; i++) weightedList.add(0); // 15 times 0
            for (int i = 0; i < 7; i++) weightedList.add(1);  // 7 times 1
            for (int i = 0; i < 3; i++) weightedList.add(2);  // 3 times 2
            Random random=new Random();
            int index = random.nextInt(weightedList.size());
            return weightedList.get(index);
        }

        


}
