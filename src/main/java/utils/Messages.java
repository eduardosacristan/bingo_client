package utils;

import javafx.scene.control.Alert;

/**
 * The Messages class contains static methods to show different messages
 * to the user
 */
public class Messages {

    /**
     * Static method that shows errors in an dialog window
     * @param header String with the content of the header of the error
     * @param message String with the body of the error
     */
    public static void showError(String header, String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle("ERROR");
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        dialog.showAndWait();
    }


}
