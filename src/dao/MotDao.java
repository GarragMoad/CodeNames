package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.polynamesDatabase;
import models.Carte;
import models.Mot;

public class MotDao {
     private final polynamesDatabase database;

    public MotDao() throws SQLException {
        this.database = new polynamesDatabase();
    }

    public List<String> findAll() {
        List<String> mots = new ArrayList<String>();
        try {
            String query = "SELECT Mot FROM mot";
            PreparedStatement statement = this.database.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                mots.add(resultSet.getString("mot"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction findAll() de MotDao.java");
        }
        return mots;
    }

    public List<String> getGrille(){
        List <String> mots=findAll();
        List <String> grille = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            int randomIndex = (int) (Math.random() * mots.size());
            String randomMot = mots.get(randomIndex);
            grille.add(randomMot);
            mots.remove(randomIndex);
        }
        return grille;
    }

    public String getMot(int id){
        try {
            String query = "SELECT Mot FROM mot WHERE id = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("mot");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getMot() de MotDao.java");
        }
        return null;
    }

    public int getId(String mot) {
        try {
            String query = "SELECT id FROM mot WHERE mot = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setString(1, mot);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getId() de MotDao.java");
        }
        return -1;
    }

}
