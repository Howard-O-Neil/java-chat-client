package application.views;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class AppScene extends HBox {

  public AppScene() {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/views/app.fxml")
    );
    this.getStylesheets()
      .add(getClass().getResource("/styles/app_style.css").toExternalForm());

    loader.setRoot(this);
    loader.setController(this);
    
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.getChildren().add(new Login());
  }
}
