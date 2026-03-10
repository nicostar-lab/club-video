package controllers;

import config.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Abonne;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AbonneController implements BaseController, RouteAwareController {
    
    
    
    @FXML private TextField txtNomAbonne;
    @FXML private TextField txtAdresseAbonne;
    @FXML private TableView<Abonne> tableAbonnes;
    @FXML private TableColumn<Abonne, Integer> colNumAbonne;
    @FXML private TableColumn<Abonne, String> colNomAbonne;
    @FXML private TableColumn<Abonne, String> colAdresseAbonne;
    @FXML private TableColumn<Abonne, Date> colDateAbonnement;
    @FXML private TableColumn<Abonne, Date> colDateEntree;
    @FXML private TableColumn<Abonne, Integer> colNombreLocation;
    
    @Override
    public void setSceneManager(SceneManager sceneManager) {
       
    }

    @Override
    public void onSceneShown(String sceneName) {
        if ("Abonne".equals(sceneName)) {
            chargerAbonnes();
        }
    }
    
    @FXML
    public void initialize() {
        colNumAbonne.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumAbonne()).asObject());
        colNomAbonne.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomAbonne()));
        colAdresseAbonne.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAdresseAbonne()));
        colDateAbonnement.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateAbonnement()));
        colDateEntree.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateEntree()));
        colNombreLocation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNombreLocation()).asObject());
        
        tableAbonnes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNomAbonne.setText(newSelection.getNomAbonne());
                txtAdresseAbonne.setText(newSelection.getAdresseAbonne());
            }
        });
    }
    
    @FXML
    private void chargerAbonnes() {
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "SELECT * FROM abonne";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Abonne> abonnes = FXCollections.observableArrayList();
            while (rs.next()) {
                Abonne a = new Abonne(
                    rs.getInt("numAbonne"),
                    rs.getString("nomAbonne"),
                    rs.getString("adresseAbonne"),
                    rs.getDate("dateAbonnement"),
                    rs.getDate("dateEntree"),
                    rs.getInt("nombreLocation")
                );
                abonnes.add(a);
            }
            
            tableAbonnes.setItems(abonnes);
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les abonnés: " + e.getMessage());
        }
    }
    
    @FXML
    private void ajouterAbonne() {
        if (txtNomAbonne.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom est obligatoire");
            return;
        }
        
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "INSERT INTO abonne (nomAbonne, adresseAbonne, dateAbonnement, dateEntree, nombreLocation) VALUES (?, ?, CURDATE(), CURDATE(), 0)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, txtNomAbonne.getText());
            pstmt.setString(2, txtAdresseAbonne.getText());
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            
            showAlert("Succès", "Abonné ajouté avec succès");
            viderChamps();
            chargerAbonnes();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible d'ajouter l'abonné: " + e.getMessage());
        }
    }
    
    @FXML
    private void modifierAbonne() {
        Abonne selected = tableAbonnes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonné à modifier");
            return;
        }
        
        if (txtNomAbonne.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom est obligatoire");
            return;
        }
        
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "UPDATE abonne SET nomAbonne=?, adresseAbonne=? WHERE numAbonne=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, txtNomAbonne.getText());
            pstmt.setString(2, txtAdresseAbonne.getText());
            pstmt.setInt(3, selected.getNumAbonne());
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            
            showAlert("Succès", "Abonné modifié avec succès");
            viderChamps();
            chargerAbonnes();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de modifier l'abonné: " + e.getMessage());
        }
    }
    
    @FXML
    private void supprimerAbonne() {
        Abonne selected = tableAbonnes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonné à supprimer");
            return;
        }
        
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "DELETE FROM abonne WHERE numAbonne=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, selected.getNumAbonne());
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            
            showAlert("Succès", "Abonné supprimé avec succès");
            viderChamps();
            chargerAbonnes();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de supprimer l'abonné: " + e.getMessage());
        }
    }
    
    @FXML
    private void actualiserTable() {
        chargerAbonnes();
    }
    
    @FXML
    private void viderChamps() {
        txtNomAbonne.clear();
        txtAdresseAbonne.clear();
        tableAbonnes.getSelectionModel().clearSelection();
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
