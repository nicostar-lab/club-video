package controllers;

import config.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeController implements BaseController, RouteAwareController {

   

    @FXML
    private Label cassettesCountLabel;

    @FXML
    private Label abonnesCountLabel;

    @FXML
    private Label locationsCountLabel;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        
    }

    @Override
    public void onSceneShown(String sceneName) {
        if ("Home".equals(sceneName)) {
            chargerStatistiques();
        }
    }

    private void chargerStatistiques() {
        try (Connection conn = new DatabaseConnection().getConnection()) {
            cassettesCountLabel.setText(String.valueOf(executeCountQuery(conn, "SELECT COUNT(*) FROM cassette")));
            abonnesCountLabel.setText(String.valueOf(executeCountQuery(conn, "SELECT COUNT(*) FROM abonne")));
            locationsCountLabel.setText(String.valueOf(executeCountQuery(conn, "SELECT COUNT(*) FROM location")));
        } catch (SQLException e) {
            cassettesCountLabel.setText("--");
            abonnesCountLabel.setText("--");
            locationsCountLabel.setText("--");
            e.printStackTrace();
        }
    }

    private int executeCountQuery(Connection conn, String query) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }
}
