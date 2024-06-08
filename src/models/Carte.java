package models;

public class Carte {
    private int id;
    private String mot;
    private Couleur couleur;
    String code_partie;
    private int posX;
    private int posY;
    private boolean etat_carte;
    private String indice;

    public Carte(int id, int posX,  int posY,boolean etat ,String code_partie, String mot, Couleur couleur, String indice) {
        this.id = id;
        this.mot = mot;
        this.couleur = couleur;
        this.code_partie=code_partie;
        this.posX = posX;
        this.posY = posY;
        this.etat_carte = etat;
        this.indice = indice;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMot() {
        return mot;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }
    public String getCodePartie() {
        return code_partie;
    }

    public void setCodePartie(String code_partie) {
        this.code_partie = code_partie;
    }

    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isEtat_carte() {
        return etat_carte;
    }

    public void setEtat_carte(boolean etat_carte) {
        this.etat_carte = etat_carte;
    }

    public String getIndice() {
        return indice;
    }
    public void setIndice(String indice) {
        this.indice = indice;
    }
    

}
