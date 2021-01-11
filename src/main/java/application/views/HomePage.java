package application.views;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import application.Utils.ImageHelper;
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

    ImageView img = new ImageView();
    img.setImage(ImageHelper.createImage("https://i.giphy.com/media/xT0GqetcAXDkrGkire/source.gif"));
    this.getChildren().add(img);
    
    managedProperty().bind(visibleProperty());
  }

  // image Of Gif
  
}
