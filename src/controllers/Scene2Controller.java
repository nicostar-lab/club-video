package controllers;

import javafx.fxml.FXML;

public class Scene2Controller implements BaseController {
    
    private SceneManager sceneManager;
    
    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    
    @FXML
    private void backToHome() {
        sceneManager.switchScene("Home");
    }
}
