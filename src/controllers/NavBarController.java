package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NavBarController implements BaseController, RouteAwareController {

    private static final String ACTIVE_STYLE = "-fx-background-color: black;";
    private static final String INACTIVE_STYLE = "-fx-background-color: red;";

    private SceneManager sceneManager;

    @FXML
    private Button homeButton;

    @FXML
    private Button cassetteButton;

    @FXML
    private Button locationButton;

    @FXML
    private Button abonneButton;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void goHome(ActionEvent event) {
        sceneManager.switchScene("Home");
    }

    @FXML
    private void goCassette(ActionEvent event) {
        sceneManager.switchScene("Scene1");
    }

    @FXML
    private void goLocation(ActionEvent event) {
        sceneManager.switchScene("Scene2");
    }

    @FXML
    private void goAbonne(ActionEvent event) {
        sceneManager.switchScene("Home");
    }

    @FXML
    private void logout(ActionEvent event) {
        sceneManager.switchScene("Login");
    }

    @Override
    public void onSceneShown(String sceneName) {
        setAllInactive();

        if ("Home".equals(sceneName)) {
            homeButton.setStyle(ACTIVE_STYLE);
        } else if ("Scene1".equals(sceneName)) {
            cassetteButton.setStyle(ACTIVE_STYLE);
        } else if ("Scene2".equals(sceneName)) {
            locationButton.setStyle(ACTIVE_STYLE);
        }
    }

    private void setAllInactive() {
        homeButton.setStyle(INACTIVE_STYLE);
        cassetteButton.setStyle(INACTIVE_STYLE);
        locationButton.setStyle(INACTIVE_STYLE);
        abonneButton.setStyle(INACTIVE_STYLE);
    }
}
