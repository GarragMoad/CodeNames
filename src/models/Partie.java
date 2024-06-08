package models;

import java.sql.Date;

public class Partie {
        private int id;
        private String codeUnique;
        private Date dateCreation;
        private boolean etatPartie;
        private int score;

        public Partie(int id, String codeUnique, Date dateCreation, boolean etatPartie, int score) {
            this.id = id;
            this.codeUnique = codeUnique;
            this.dateCreation = dateCreation;
            this.etatPartie = etatPartie;
            this.score = score;
        }

        // Getters and setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCodeUnique() {
            return codeUnique;
        }

        public void setCodeUnique(String codeUnique) {
            this.codeUnique = codeUnique;
        }

        public Date getDateCreation() {
            return dateCreation;
        }

        public void setDateCreation(Date dateCreation) {
            this.dateCreation = dateCreation;
        }

        public boolean isEtatPartie() {
            return etatPartie;
        }

        public void setEtatPartie(boolean etatPartie) {
            this.etatPartie = etatPartie;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
}

