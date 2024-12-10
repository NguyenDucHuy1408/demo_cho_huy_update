package javaFx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminSystem {

    @FXML
    private Label mainLabel;

    @FXML
    private void goToDashboard() {
        mainLabel.setText("Dashboard is under development.");
    }

    @FXML
    private void goToUsers() {
        mainLabel.setText("Users management section.");
    }

    @FXML
    private void goToDocuments() {
        mainLabel.setText("Documents management section.");
    }

    @FXML
    private void goToTransactions() {
        mainLabel.setText("Transactions management section.");
    }

    @FXML
    private void goToReviews() {
        mainLabel.setText("Reviews management section.");
    }
}
