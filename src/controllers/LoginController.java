package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


import config.DatabaseConnection;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController implements BaseController {

    private SceneManager sceneManager;

    @FXML
    private TextField emailtextfield;

    @FXML
    private Button loginbutton;

    @FXML
    private Label messagefield;

    @FXML
    private PasswordField passwordpasswordfield;

    @FXML
    void handleclick(ActionEvent event) {

        if(emailtextfield.getText().isEmpty() || passwordpasswordfield.getText().isEmpty()) {
            messagefield.setText("Entrer votre username et mot de passe !");
        } else {
            validate();
        }
    }

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private void validate() {
        String username = emailtextfield.getText();
        String password = passwordpasswordfield.getText();
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        DatabaseConnection connectionnow = new DatabaseConnection();
        Connection connectdb = connectionnow.getConnection();

        Statement stmt = null;
        String req = "SELECT COUNT(1) FROM utilisateur WHERE login = '"+username+"' AND motDePasse = '"+password+"'";

        try {
            pause.setOnFinished(event ->{
                
            });
            stmt = connectdb.createStatement();
            ResultSet res = stmt.executeQuery(req);

            while(res.next()) {
                if(res.getInt(1) == 1) {
                    messagefield.setText("Connexion reussie !");
                    messagefield.setStyle("-fx-text-fill: green");
                    pause.play();
                    sceneManager.switchScene("Home");
                    
                    
                } else {
                    messagefield.setText("username ou mot de passe incorrect !");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        
}
}
