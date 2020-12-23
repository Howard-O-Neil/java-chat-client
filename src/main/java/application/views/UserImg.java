package application.views;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class UserImg extends Circle {

  public UserImg() {
    super();
  }

  ImageView imv;
  Circle clipshape;

  public void load(String url, float radius) {
    Image img = new Image(url);
    this.setFill(new ImagePattern(img));
    this.setRadius(radius);
  }
}
