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

    public CarteDao() throws SQLException {
        this.database = new polynamesDatabase();
        this.partieDao= new PartieDao();
        this.motDao= new MotDao();
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

                    String code_partie=this.partieDao.getCodePartie(id_Partie);
                    String mot=this.motDao.getMot(idMot);

                    cartes.add(new Carte(foundId,positionX,positionY,etat_carte,code_partie,mot,Couleur.getByIndex(id_couleur-1)));
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

                    String code_partie=this.partieDao.getCodePartie(idPartie);
                    String mot=this.motDao.getMot(idMot);

                    return new Carte(foundId,posX,posY,etat_carte,code_partie,mot,Couleur.getByIndex(id_couleur-1));
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

        public ArrayList<Carte> getGrillePartie(String code){
            ArrayList <Carte> grille = new ArrayList<Carte>();
            int idPartie = this.partieDao.getId(code);
            try {
                String query = "SELECT * FROM carte WHERE idPartie = ?";
                PreparedStatement statement = this.database.prepareStatement(query);
                statement.setInt(1, idPartie);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) { 
                    int foundId = resultSet.getInt("id");
                    boolean etat_carte = resultSet.getBoolean("isCheck");
                    int idMot = resultSet.getInt("idMot");
                    int id_couleur = resultSet.getInt("idCouleur");
                    int posX = resultSet.getInt("posX");
                    int posY = resultSet.getInt("posY");
                    String mot=this.motDao.getMot(idMot);
                    grille.add(new Carte(foundId,posX,posY,etat_carte,code,mot,Couleur.getByIndex(id_couleur-1))) ;
                    
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("erreur dans la fonction getGrillePartie() de CarteDao.java");
            }
            System.out.println("Grille de la partie "+code+" : "+grille);
            return grille;
        }

        //fonction qui attribue les couleurs aléatoirement aux cartes de la partie
        private int getRandomInteger(){
            List<Integer> weightedList = new ArrayList<>();
            for (int i = 0; i < 8; i++) weightedList.add(0); // 8 times BLUE
            for (int i = 0; i < 15; i++) weightedList.add(1);  // 15 times GRIS
            for (int i = 0; i < 3; i++) weightedList.add(2);  // 2 times NOIR
            Random random=new Random();
            int index = random.nextInt(weightedList.size());
            return weightedList.get(index);
        }

        public int getCouleurId(int posX, int posY, int idPartie) {
            try {
                String query = "SELECT idCouleur FROM carte WHERE posX = ? AND posY = ? AND idPartie = ?";
                PreparedStatement statement = this.database.prepareStatement(query);
                statement.setInt(1, posX);
                statement.setInt(2, posY);
                statement.setInt(3, idPartie);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("idCouleur");
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("erreur dans la fonction getCouleurId() de CarteDao.java");
            }
            return -1;
        }

        public void updateEtatCarte(int etat,int posX, int posY, int idPartie) {
            try {
                String query = "UPDATE carte SET isCheck = ? WHERE posX = ? AND posY = ? AND idPartie = ?";
                PreparedStatement statement = this.database.prepareStatement(query);
                statement.setInt(1, etat);
                statement.setInt(2, posX);
                statement.setInt(3, posY);
                statement.setInt(4, idPartie);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("erreur dans la fonction updateEtatCarte() de CarteDao.java");
            }
        }


}
