package models;

import java.sql.Date;

public class Abonne {
    private int numAbonne;
    private String nomAbonne;
    private String adresseAbonne;
    private Date dateAbonnement;
    private Date dateEntree;
    private int nombreLocation;
    
    public Abonne() {}
    
    public Abonne(int numAbonne, String nomAbonne, String adresseAbonne, Date dateAbonnement, Date dateEntree, int nombreLocation) {
        this.numAbonne = numAbonne;
        this.nomAbonne = nomAbonne;
        this.adresseAbonne = adresseAbonne;
        this.dateAbonnement = dateAbonnement;
        this.dateEntree = dateEntree;
        this.nombreLocation = nombreLocation;
    }
    
    // Getters and Setters
    public int getNumAbonne() { return numAbonne; }
    public void setNumAbonne(int numAbonne) { this.numAbonne = numAbonne; }
    
    public String getNomAbonne() { return nomAbonne; }
    public void setNomAbonne(String nomAbonne) { this.nomAbonne = nomAbonne; }
    
    public String getAdresseAbonne() { return adresseAbonne; }
    public void setAdresseAbonne(String adresseAbonne) { this.adresseAbonne = adresseAbonne; }
    
    public Date getDateAbonnement() { return dateAbonnement; }
    public void setDateAbonnement(Date dateAbonnement) { this.dateAbonnement = dateAbonnement; }
    
    public Date getDateEntree() { return dateEntree; }
    public void setDateEntree(Date dateEntree) { this.dateEntree = dateEntree; }
    
    public int getNombreLocation() { return nombreLocation; }
    public void setNombreLocation(int nombreLocation) { this.nombreLocation = nombreLocation; }
}
