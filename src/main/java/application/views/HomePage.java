package application.views;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import application.App;
import application.Utils.MediaHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class HomePage extends StackPane {

  public HomePage() {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/views/homepage.fxml")
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

    App.executor.execute(() -> {
      ImageView img = new ImageView();
      img.setImage(MediaHelper.createImage("https://i.giphy.com/media/xT0GqetcAXDkrGkire/source.gif"));

      Platform.runLater(() -> {
        this.getChildren().clear();
        this.getChildren().add(img);
      });
    });
  }

  // image Of Gif
  
}
