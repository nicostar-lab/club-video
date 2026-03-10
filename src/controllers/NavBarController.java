package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NavBarController implements BaseController, RouteAwareController {

    private static final String ACTIVE_STYLE_CLASS = "active-nav-button";

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
        sceneManager.switchScene("Cassette");
    }

    @FXML
    private void goLocation(ActionEvent event) {
        sceneManager.switchScene("Location");
    }

    @FXML
    private void goAbonne(ActionEvent event) {
        sceneManager.switchScene("Abonne");
    }

    @FXML
    private void logout(ActionEvent event) {
        sceneManager.switchScene("Login");
    }

    @Override
    public void onSceneShown(String sceneName) {
        setAllInactive();

        if ("Home".equals(sceneName)) {
            setActive(homeButton);
        } else if ("Cassette".equals(sceneName)) {
            setActive(cassetteButton);
        } else if ("Location".equals(sceneName)) {
            setActive(locationButton);
        } else if ("Abonne".equals(sceneName)) {
            setActive(abonneButton);
        }
    }

    private void setAllInactive() {
        removeActiveStyle(homeButton);
        removeActiveStyle(cassetteButton);
        removeActiveStyle(locationButton);
        removeActiveStyle(abonneButton);
    }

    private void setActive(Button button) {
        if (!button.getStyleClass().contains(ACTIVE_STYLE_CLASS)) {
            button.getStyleClass().add(ACTIVE_STYLE_CLASS);
        }
    }

    private void removeActiveStyle(Button button) {
        button.getStyleClass().remove(ACTIVE_STYLE_CLASS);
    }
}
