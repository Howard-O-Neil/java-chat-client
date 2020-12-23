package application.views;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class ProfilePage extends StackPane {

  public ProfilePage() {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/views/profilepage.fxml")
    );
    //this.getStylesheets().add(getClass().getResource("/styles/main_style.css").toExternalForm());
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    managedProperty().bind(visibleProperty());
  }
}
