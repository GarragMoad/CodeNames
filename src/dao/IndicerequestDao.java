package dao;

import java.sql.SQLException;

import database.polynamesDatabase;

public class IndicerequestDao {
    private final polynamesDatabase database;

    public IndicerequestDao() throws SQLException {
        this.database = new polynamesDatabase();
    }
    public void insertIndice(String indice, int nbMots, int idPartie, int tour){
        try {
            String query = "INSERT INTO tour (tour,indice, nbMotsSusceptible,nbMotsTrouver, idPartie) VALUES (?,?,?,0,?)";
            var statement = this.database.prepareStatement(query);
            statement.setInt(1, tour);
            statement.setString(2, indice);
            statement.setInt(3, nbMots);
            statement.setInt(4, idPartie);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction insertIndice() de IndicerequestDao.java");
        }
    }

    public int getTour(int idPartie){
        try {
            String query = "SELECT MAX(tour) FROM (SELECT tour  FROM Tour WHERE idPartie = ? GROUP BY tour)AS tourMax";
            var statement = this.database.prepareStatement(query);
            statement.setInt(1, idPartie);
            var result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("MAX(tour)");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getTour() de IndicerequestDao.java");
        }
        return 0;
    }



}
