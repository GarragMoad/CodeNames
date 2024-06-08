package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.polynamesDatabase;

public class indiceDao {

    private final polynamesDatabase database;

    public indiceDao() throws SQLException {
        this.database = new polynamesDatabase();
    }

    public String getIndice(int id) {
        try {
            String query = "SELECT * FROM indice WHERE id = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String indice = resultSet.getString("indice");
                return indice;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getIndice() de indiceDao.java");
        }
        return null;
    }

}
