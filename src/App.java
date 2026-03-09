import javafx.application.Application;
import javafx.stage.Stage;
import controllers.SceneManager;

import java.io.IOException;

public class App extends Application {
    private SceneManager sceneManager;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Video Club");
        
        
        // Initialiser le gestionnaire de scènes
        sceneManager = new SceneManager(stage);
        
        // Charger toutes les scènes depuis les fichiers FXML
        sceneManager.loadScene("Login", "/resources/fxml/Login.fxml");
        sceneManager.loadScene("Home", "/resources/fxml/Home.fxml");
        sceneManager.loadScene("Scene1", "/resources/fxml/Cassette.fxml");
        sceneManager.loadScene("Scene2", "/resources/fxml/Location.fxml");
        
        // Afficher la scène de login au démarrage
        sceneManager.switchScene("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
