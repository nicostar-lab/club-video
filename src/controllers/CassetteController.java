package controllers;

import config.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Cassette;
import models.Categorie;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CassetteController implements BaseController, RouteAwareController {
    
    private SceneManager sceneManager;
    
    @FXML private TextField txtTitre;
    @FXML private TextField txtAuteur;
    @FXML private TextField txtDuree;
    @FXML private TextField txtPrix;
    @FXML private DatePicker dateAchat;
    @FXML private ComboBox<Categorie> cbCategorie;
    @FXML private TableView<Cassette> tableCassettes;
    @FXML private TableColumn<Cassette, Integer> colNumCassette;
    @FXML private TableColumn<Cassette, Date> colDateAchat;
    @FXML private TableColumn<Cassette, String> colTitre;
    @FXML private TableColumn<Cassette, String> colAuteur;
    @FXML private TableColumn<Cassette, Integer> colDuree;
    @FXML private TableColumn<Cassette, Double> colPrix;
    @FXML private TableColumn<Cassette, String> colLibelleCategorie;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onSceneShown(String sceneName) {
        if ("Cassette".equals(sceneName)) {
            chargerCategories();
            chargerCassettes();
        }
    }

    @FXML
    public void initialize() {
        colNumCassette.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumCassette()).asObject());
        colDateAchat.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateAchat()));
        colTitre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitre()));
        colAuteur.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuteur()));
        colDuree.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getDuree()).asObject());
        colPrix.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());
        colLibelleCategorie.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLibelleCategorie()));

        tableCassettes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtTitre.setText(newVal.getTitre());
                txtAuteur.setText(newVal.getAuteur());
                txtDuree.setText(String.valueOf(newVal.getDuree()));
                txtPrix.setText(String.valueOf(newVal.getPrix()));
                dateAchat.setValue(newVal.getDateAchat().toLocalDate());
                
                for (Categorie cat : cbCategorie.getItems()) {
                    if (cat.getIdCategorie() == newVal.getIdCategorie()) {
                        cbCategorie.setValue(cat);
                        break;
                    }
                }
            }
        });
    }

    private void chargerCategories() {
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "SELECT * FROM categorie ORDER BY libelle";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            ObservableList<Categorie> categories = FXCollections.observableArrayList();
            while (rs.next()) {
                categories.add(new Categorie(
                    rs.getInt("idCategorie"),
                    rs.getString("libelle")
                ));
            }
            cbCategorie.setItems(categories);
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des catégories: " + e.getMessage());
        }
    }
    
    @FXML
    private void chargerCassettes() {
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "SELECT c.numCassette, c.dateAchat, c.titre, c.auteur, c.duree, c.prix, c.idCategorie, ca.libelle " +
                         "FROM cassette c " +
                         "LEFT JOIN categorie ca ON c.idCategorie = ca.idCategorie";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            ObservableList<Cassette> cassettes = FXCollections.observableArrayList();
            while (rs.next()) {
                cassettes.add(new Cassette(
                    rs.getInt("numCassette"),
                    rs.getDate("dateAchat"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getInt("duree"),
                    rs.getDouble("prix"),
                    rs.getInt("idCategorie"),
                    rs.getString("libelle")
                ));
            }
            tableCassettes.setItems(cassettes);
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des cassettes: " + e.getMessage());
        }
    }
    
    @FXML
    private void ajouterCassette() {
        if (!validerChamps()) {
            showAlert("Erreur de validation", "Veuillez remplir tous les champs correctement");
            return;
        }

        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "INSERT INTO cassette (dateAchat, titre, auteur, duree, prix, idCategorie) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, Date.valueOf(dateAchat.getValue()));
            pstmt.setString(2, txtTitre.getText());
            pstmt.setString(3, txtAuteur.getText());
            pstmt.setInt(4, Integer.parseInt(txtDuree.getText()));
            pstmt.setDouble(5, Double.parseDouble(txtPrix.getText()));
            pstmt.setInt(6, cbCategorie.getValue().getIdCategorie());
            pstmt.executeUpdate();

            showAlert("Succès", "Cassette ajoutée avec succès");
            viderChamps();
            chargerCassettes();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }
    
    @FXML
    private void modifierCassette() {
        Cassette selected = tableCassettes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une cassette à modifier");
            return;
        }

        if (!validerChamps()) {
            showAlert("Erreur de validation", "Veuillez remplir tous les champs correctement");
            return;
        }

        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "UPDATE cassette SET dateAchat = ?, titre = ?, auteur = ?, duree = ?, prix = ?, idCategorie = ? WHERE numCassette = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, Date.valueOf(dateAchat.getValue()));
            pstmt.setString(2, txtTitre.getText());
            pstmt.setString(3, txtAuteur.getText());
            pstmt.setInt(4, Integer.parseInt(txtDuree.getText()));
            pstmt.setDouble(5, Double.parseDouble(txtPrix.getText()));
            pstmt.setInt(6, cbCategorie.getValue().getIdCategorie());
            pstmt.setInt(7, selected.getNumCassette());
            pstmt.executeUpdate();

            showAlert("Succès", "Cassette modifiée avec succès");
            viderChamps();
            chargerCassettes();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }
    
    @FXML
    private void supprimerCassette() {
        Cassette selected = tableCassettes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une cassette à supprimer");
            return;
        }

        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            String query = "DELETE FROM cassette WHERE numCassette = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, selected.getNumCassette());
            pstmt.executeUpdate();

            showAlert("Succès", "Cassette supprimée avec succès");
            viderChamps();
            chargerCassettes();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @FXML
    private void actualiserTable() {
        chargerCassettes();
    }
    
    @FXML
    private void viderChamps() {
        txtTitre.clear();
        txtAuteur.clear();
        txtDuree.clear();
        txtPrix.clear();
        dateAchat.setValue(LocalDate.now());
        cbCategorie.setValue(null);
        tableCassettes.getSelectionModel().clearSelection();
    }

    private boolean validerChamps() {
        if (txtTitre.getText().trim().isEmpty() || txtAuteur.getText().trim().isEmpty() || 
            cbCategorie.getValue() == null || dateAchat.getValue() == null) {
            return false;
        }
        try {
            Integer.parseInt(txtDuree.getText());
            Double.parseDouble(txtPrix.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
