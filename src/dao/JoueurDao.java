package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.polynamesDatabase;

public class JoueurDao {
        private final polynamesDatabase database;

    public JoueurDao() throws SQLException {
        this.database = new polynamesDatabase();
    }

    public int CreateJoueur(String nom, int role) {
        try {
            if(checkJoueur(nom)){
                return -1;
            }
            String query = "INSERT INTO Joueur (nom, role) VALUES (?, ?)";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setString(1, nom); // Utilisation du pseudo passé en paramètre
            if (role == 1) {
                statement.setString(2, "Maitre de mots");
            } else {
                statement.setString(2, "Maitre d intuition");
            }
            statement.executeUpdate();
            statement.close();
            return getId(nom);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans la fonction CreateJoueur() de JoueurDao.java");
        }
        return -1;
    }

    public int getId(String pseudo){
        int id = -1;
        try {
            String query = "SELECT id FROM joueur WHERE nom = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setString(1, pseudo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getId() de JoueurDao.java");
        }
        return id;
    }

    public String getRole(int id){
        String role = "";
        try {
            String query = "SELECT role FROM joueur WHERE id = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction getRole() de JoueurDao.java");
        }
        return role;
    }

    public boolean checkJoueur(String nom){
        try {
            String query = "SELECT * FROM joueur WHERE nom = ?";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setString(1, nom);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                statement.close();
                return true;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction checkJoueur() de JoueurDao.java");
        }
        return false;
    }

}
