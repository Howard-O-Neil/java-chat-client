package application.views;

import application.App;
import application.Utils.ImageHelper;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;

public class MessageText extends GridPane {

  @FXML
  TextFlow text_flow;

  @FXML
  Label text_label;

  ImageView imgView;

  public MessageText() {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/views/messagetext.fxml")
    );
    this.getStylesheets()
      .add(
        App.class.getResource("/styles/messagetext_style.css").toExternalForm()
      );
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setAsImageDisplay(String url) {
    this.getChildren().clear();
    
    this.imgView = new ImageView();
    this.imgView.setImage(ImageHelper.createImage(url));
    this.add(imgView, 1, 0);
  }

  public void setAsSend() {
    this.setAlignment(Pos.BASELINE_RIGHT);
    text_label.getStyleClass().clear();
    text_label.getStyleClass().add("msg-text-send");
  }

  public void setAsReceive() {
    this.setAlignment(Pos.BASELINE_LEFT);
    text_label.getStyleClass().clear();
    text_label.getStyleClass().add("msg-text-receive");
  }

  public void setText(String text) {
    text_label.setText(text);
  }
}
