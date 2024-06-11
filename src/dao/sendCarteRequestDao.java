package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.polynamesDatabase;

public class sendCarteRequestDao {
     private final polynamesDatabase database;

    public sendCarteRequestDao() throws SQLException {
        this.database = new polynamesDatabase();
    }

    public int getTour(int codePartie){
        try {
            String query = "SELECT tour FROM tour WHERE idPartie = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, codePartie);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("tour");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getTour() de sendCarteRequestDao.java");
        }
        return -1;
    }

    public int getNbMotsSuscpetible(int codePartie){
        try {
            String query = "SELECT nbMotsSusceptible FROM tour WHERE idPartie = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, codePartie);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("nbMotsSusceptible");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getNbMotsSuscpetible() de sendCarteRequestDao.java");
        }
        return -1;
    }

    public int getNbMotsTrouver(int codePartie){
        try {
            String query = "SELECT nbMotsTrouver FROM tour WHERE idPartie = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, codePartie);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("nbMotsTrouver");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getNbMotsTrouve() de sendCarteRequestDao.java");
        }
        return -1;
    }

    public int updateTour(int tour, int codePartie){
        try {
            String query = "UPDATE tour SET tour = ? WHERE idPartie = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, tour);
            statement.setInt(2, codePartie);
            int resultSet = statement.executeUpdate();
            statement.close();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction updateTour() de sendCarteRequestDao.java");
        }
        return -1;
    }

    public int updateNbMotsTrouver(int nbMotsTrouver, int codePartie){
        try {
            String query = "UPDATE tour SET nbMotsTrouver = ? WHERE idPartie = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, nbMotsTrouver);
            statement.setInt(2, codePartie);
            int resultSet = statement.executeUpdate();
            statement.close();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction updateNbMotsTrouver() de sendCarteRequestDao.java");
        }
        return -1;
    }

}
