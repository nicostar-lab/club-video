import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import controllers.SceneManager;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    private SceneManager sceneManager;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Film Rent: Gestion de location de films");

        Image logo = new Image(Objects.requireNonNull(
            getClass().getResourceAsStream("logo.png"),
            "Resource not found:logo.png"
        ));
        stage.getIcons().add(logo);

        // Initialiser le gestionnaire de scènes
        sceneManager = new SceneManager(stage);
        
        // Charger toutes les scènes depuis les fichiers FXML
        sceneManager.loadScene("Login", "/resources/fxml/Login.fxml");
        sceneManager.loadScene("Home", "/resources/fxml/Home.fxml");
        sceneManager.loadScene("Cassette", "/resources/fxml/Cassette.fxml");
        sceneManager.loadScene("Location", "/resources/fxml/Location.fxml");
        sceneManager.loadScene("Abonne", "/resources/fxml/Abonne.fxml");
        
        // Afficher la scène de login au démarrage
        sceneManager.switchScene("Home");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
