package controllers;

import config.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Location;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationController implements BaseController, RouteAwareController {
    
    private SceneManager sceneManager;
    
    @FXML private TextField txtNumAbonne;
    @FXML private TextField txtNumCassette;
    
    @FXML private TableView<Location> tableLocations;
    @FXML private TableColumn<Location, Integer> colNumAbonne;
    @FXML private TableColumn<Location, String> colNomAbonne;
    @FXML private TableColumn<Location, Integer> colNumCassette;
    @FXML private TableColumn<Location, String> colTitreCassette;
    @FXML private TableColumn<Location, Date> colDateLocation;
    @FXML private TableColumn<Location, Date> colDateRetour;
    
    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onSceneShown(String sceneName) {
        if ("Location".equals(sceneName)) {
            chargerLocations();
        }
    }
    
    @FXML
    public void initialize() {
        colNumAbonne.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumAbonne()).asObject());
        colNomAbonne.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomAbonne()));
        colNumCassette.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumCassette()).asObject());
        colTitreCassette.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitreCassette()));
        colDateLocation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateLocation()));
        colDateRetour.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateRetour()));
        
        tableLocations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNumAbonne.setText(String.valueOf(newSelection.getNumAbonne()));
                txtNumCassette.setText(String.valueOf(newSelection.getNumCassette()));
            }
        });
    }
    
    @FXML
    private void chargerLocations() {
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "SELECT l.numAbonne, a.nomAbonne, l.numCassette, c.titre as titreCassette, l.dateLocation, l.dateRetour " +
                          "FROM location l " +
                          "LEFT JOIN abonne a ON l.numAbonne = a.numAbonne " +
                          "LEFT JOIN cassette c ON l.numCassette = c.numCassette";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Location> locations = FXCollections.observableArrayList();
            while (rs.next()) {
                Location l = new Location(
                    rs.getInt("numAbonne"),
                    rs.getInt("numCassette"),
                    rs.getString("nomAbonne"),
                    rs.getString("titreCassette"),
                    rs.getDate("dateLocation"),
                    rs.getDate("dateRetour")
                );
                locations.add(l);
            }
            
            tableLocations.setItems(locations);
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les locations: " + e.getMessage());
        }
    }
    
    @FXML
    private void louerCassette() {
        if (txtNumAbonne.getText().trim().isEmpty() || txtNumCassette.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez saisir le n° abonné et le n° cassette");
            return;
        }
        
        try {
            int numAbonne = Integer.parseInt(txtNumAbonne.getText());
            int numCassette = Integer.parseInt(txtNumCassette.getText());
            
            // Vérifier si l'abonné n'a pas déjà 3 locations
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            
            String checkQuery = "SELECT nombreLocation FROM abonne WHERE numAbonne = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, numAbonne);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                int nombreLocation = rs.getInt("nombreLocation");
                if (nombreLocation >= 3) {
                    showAlert("Erreur", "Cet abonné a déjà 3 cassettes en location");
                    rs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }
            } else {
                showAlert("Erreur", "Abonné introuvable");
                rs.close();
                checkStmt.close();
                conn.close();
                return;
            }
            rs.close();
            checkStmt.close();
            
            // Ajouter la location
            String query = "INSERT INTO location (numAbonne, numCassette, dateLocation) VALUES (?, ?, CURDATE())";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, numAbonne);
            pstmt.setInt(2, numCassette);
            pstmt.executeUpdate();
            pstmt.close();
            
            // Incrémenter nombreLocation de l'abonné
            String updateQuery = "UPDATE abonne SET nombreLocation = nombreLocation + 1 WHERE numAbonne = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, numAbonne);
            updateStmt.executeUpdate();
            updateStmt.close();
            
            conn.close();
            
            showAlert("Succès", "Location enregistrée avec succès");
            viderChamps();
            chargerLocations();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les numéros doivent être des nombres valides");
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible d'enregistrer la location: " + e.getMessage());
        }
    }
    
    @FXML
    private void retournerCassette() {
        Location selected = tableLocations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une location à retourner");
            return;
        }
        
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            
            // Supprimer la location (clé primaire composite: numAbonne, numCassette)
            String query = "DELETE FROM location WHERE numAbonne=? AND numCassette=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, selected.getNumAbonne());
            pstmt.setInt(2, selected.getNumCassette());
            pstmt.executeUpdate();
            pstmt.close();
            
            // Décrémenter nombreLocation de l'abonné
            String updateQuery = "UPDATE abonne SET nombreLocation = nombreLocation - 1 WHERE numAbonne = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, selected.getNumAbonne());
            updateStmt.executeUpdate();
            updateStmt.close();
            
            conn.close();
            
            showAlert("Succès", "Retour enregistré avec succès");
            viderChamps();
            chargerLocations();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible d'enregistrer le retour: " + e.getMessage());
        }
    }
    
    @FXML
    private void actualiserTable() {
        chargerLocations();
    }
    
    @FXML
    private void viderChamps() {
        txtNumAbonne.clear();
        txtNumCassette.clear();
        tableLocations.getSelectionModel().clearSelection();
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
