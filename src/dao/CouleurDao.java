package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.polynamesDatabase;

public class CouleurDao {
        private final polynamesDatabase database;

    public CouleurDao() throws SQLException {
        this.database = new polynamesDatabase();
    }

    public int getId(String couleur){
        try {
            String query = "SELECT id FROM couleur WHERE couleurNom = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setString(1, couleur);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getCouleur() de couleurDAO.java");
        }
        return -1;
    }
}
