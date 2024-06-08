package models;

public enum Couleur {
    BLEU,
    GRIS,
    NOIR;

    public static Couleur getByIndex(int index) {
        return values()[index];
    }
}
