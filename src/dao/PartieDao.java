package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.polynamesDatabase;
import java.util.Date;
import java.util.Random;

public class PartieDao {
    private final polynamesDatabase database;

    public PartieDao() throws SQLException {
        this.database = new polynamesDatabase();
    }

    public String CreatePartie() {
        String code="";
        try {
            code = generateCodeUnique();
            String query = "INSERT INTO partie (code_unique,date_creation,etat_partie,score) VALUES (?,?,?,?)";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setString(1, code);
            Date d = new Date();
            statement.setDate(2, new java.sql.Date(d.getTime()));
            statement.setBoolean(3, true);
            statement.setInt(4, 0);
            statement.executeUpdate();
            statement.close();
             statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction CreatePartie() de PartieDao.java");
        }
        return code;
    }
    
    public int getId(String code) {
        try {
            String query = "SELECT id FROM partie WHERE code_unique = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getId() de PartieDao.java");
        }
        return -1;
    }

    public String getCodePartie(int id){
        try {
            String query = "SELECT code_unique FROM partie WHERE id = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("code_unique");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getCodePartie() de PartieDao.java");
        }
        return null;
    }

    public String generateCodeUnique() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 6;
        StringBuilder code = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }


}
