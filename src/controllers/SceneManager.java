package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Stage stage;
    private Map<String, Scene> scenes = new HashMap<>();
    private Map<String, Object[]> sceneControllers = new HashMap<>();
    
    public SceneManager(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Charge une scène depuis un fichier FXML
     */
    public void loadScene(String name, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        // Passer le SceneManager au contrôleur principal et aux contrôleurs inclus (fx:include)
        injectIfBaseController(loader.getController());
        for (Object value : loader.getNamespace().values()) {
            injectIfBaseController(value);
        }

        sceneControllers.put(name, loader.getNamespace().values().toArray());
        scenes.put(name, scene);
    }

    private void injectIfBaseController(Object controller) {
        if (controller instanceof BaseController) {
            ((BaseController) controller).setSceneManager(this);
        }
    }
    
    /**
     * Change la scène affichée
     */
    public void switchScene(String name) {
        Scene scene = scenes.get(name);
        if (scene != null) {
            stage.setScene(scene);
            notifySceneShown(name);
        } else {
            System.err.println("Scene '" + name + "' not found!");
        }
    }

    private void notifySceneShown(String sceneName) {
        Object[] controllers = sceneControllers.get(sceneName);
        if (controllers == null) {
            return;
        }

        for (Object controller : controllers) {
            if (controller instanceof RouteAwareController) {
                ((RouteAwareController) controller).onSceneShown(sceneName);
            }
        }
    }
    
    public Stage getStage() {
        return stage;
    }
}
