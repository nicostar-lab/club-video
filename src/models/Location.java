package models;

import java.sql.Date;

public class Location {
    private int numAbonne;
    private int numCassette;
    private String nomAbonne;
    private String titreCassette;
    private Date dateLocation;
    private Date dateRetour;
    
    public Location() {}
    
    public Location(int numAbonne, int numCassette, String nomAbonne, String titreCassette, Date dateLocation, Date dateRetour) {
        this.numAbonne = numAbonne;
        this.numCassette = numCassette;
        this.nomAbonne = nomAbonne;
        this.titreCassette = titreCassette;
        this.dateLocation = dateLocation;
        this.dateRetour = dateRetour;
    }
    
    // Getters and Setters
    public int getNumAbonne() { return numAbonne; }
    public void setNumAbonne(int numAbonne) { this.numAbonne = numAbonne; }
    
    public int getNumCassette() { return numCassette; }
    public void setNumCassette(int numCassette) { this.numCassette = numCassette; }
    
    public String getNomAbonne() { return nomAbonne; }
    public void setNomAbonne(String nomAbonne) { this.nomAbonne = nomAbonne; }
    
    public String getTitreCassette() { return titreCassette; }
    public void setTitreCassette(String titreCassette) { this.titreCassette = titreCassette; }
    
    public Date getDateLocation() { return dateLocation; }
    public void setDateLocation(Date dateLocation) { this.dateLocation = dateLocation; }
    
    public Date getDateRetour() { return dateRetour; }
    public void setDateRetour(Date dateRetour) { this.dateRetour = dateRetour; }
}
