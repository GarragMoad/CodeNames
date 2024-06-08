package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.polynamesDatabase;

public class JoueurPartieDao {
    private final polynamesDatabase database;

    public JoueurPartieDao() throws SQLException {
        this.database = new polynamesDatabase();
    }

    public boolean InsertJoueurPartie(int idJoueur,int idPartie){
        try {
            String query = "INSERT INTO joueurpartie (idJoueur,idPartie) VALUES (?,?)";
            PreparedStatement statement = this.database.prepareStatement(query);
            statement.setInt(1, idJoueur);
            statement.setInt(2, idPartie);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erreur dans la fonction CreatePartie() de  de JoueurPartieDao.java");
        }
        return false;
    }
    

}
