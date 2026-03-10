package models;

import java.sql.Date;

public class Cassette {
    private int numCassette;
    private Date dateAchat;
    private String titre;
    private String auteur;
    private int duree;
    private double prix;
    private int idCategorie;
    private String libelleCategorie;
    
    public Cassette() {}
    
    public Cassette(int numCassette, Date dateAchat, String titre, String auteur, int duree, double prix, int idCategorie, String libelleCategorie) {
        this.numCassette = numCassette;
        this.dateAchat = dateAchat;
        this.titre = titre;
        this.auteur = auteur;
        this.duree = duree;
        this.prix = prix;
        this.idCategorie = idCategorie;
        this.libelleCategorie = libelleCategorie;
    }
    
    // Getters and Setters
    public int getNumCassette() { return numCassette; }
    public void setNumCassette(int numCassette) { this.numCassette = numCassette; }
    
    public Date getDateAchat() { return dateAchat; }
    public void setDateAchat(Date dateAchat) { this.dateAchat = dateAchat; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    public int getIdCategorie() { return idCategorie; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }
    
    public String getLibelleCategorie() { return libelleCategorie; }
    public void setLibelleCategorie(String libelleCategorie) { this.libelleCategorie = libelleCategorie; }
}
