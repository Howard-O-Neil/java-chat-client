package application.views;

import application.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class GifPicker extends VBox {
  GifPicker() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/gifPicker.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
