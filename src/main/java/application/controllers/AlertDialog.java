package application.controllers;

public class AlertDialog {

  public static void makeAler(
    String title,
    String header,
    String content,
    javafx.scene.control.Alert.AlertType type
  ) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    alert.showAndWait();
  }
}
