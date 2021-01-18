package application.views;

import application.App;
import application.Utils.MediaHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.springframework.core.ExceptionDepthComparator;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

public class MessageText extends GridPane {

  @FXML
  TextFlow text_flow;

  @FXML
  Label text_label;

  Hyperlink fileView;
  ImageView imgView;
  MediaView mediaView;

  private final String displayUrlPrefix = "https://i.giphy.com/";
  private final String displayUrllPostFix = "/giphy.mp4";
  private final Integer maxWidthMedia = 350;
  private final Integer maxHeightMedia = 235;
  private final Integer minWidthMedia = 30;
  private final Integer minHeightMedia = 20;

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

  private Image handleImage(String fileName) throws Exception {
    String fullSizeUrl = App.cdnUrl + "cdn/" + fileName;
    Image fullSizeImg = MediaHelper.createImage(fullSizeUrl);
    
    Integer width = (int)fullSizeImg.getWidth();
    Integer height = (int)fullSizeImg.getHeight();

    Integer displayWidth = maxWidthMedia, 
      displayHeight = (int) (((float) maxWidthMedia / (float)width) * (float)height);
    
    if (displayHeight > maxHeightMedia) {
      displayWidth -= (displayHeight - maxHeightMedia);
      displayHeight = maxHeightMedia;
    }
    if (displayWidth < minWidthMedia) displayWidth = minWidthMedia;
    if (displayHeight < minHeightMedia) displayHeight = minHeightMedia;

    displayWidth = maxWidthMedia;
    String mainUrl = App.cdnUrl + "cdn/" + fileName
      + "?width=" + displayWidth.toString()
      + "&height=" + displayHeight.toString();

    return new Image(mainUrl);
  }

  public void setAsImageDisplay(String fileName) {
    this.getChildren().clear();
    
    App.executor.execute(() -> {
      Image img = null;
      
      try {
        img = handleImage(fileName);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      this.imgView = new ImageView(img);
      this.imgView.setCursor(Cursor.HAND);
      this.imgView.setOnMouseClicked(e -> {
        String fullSizeUrl = App.cdnUrl + "cdn/" + fileName;
        App._instance.getHostServices().showDocument(fullSizeUrl);
      });

      Platform.runLater(() -> {
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setMinWidth(imgView.getImage().getWidth() + 5);
        pane.setMinHeight(imgView.getImage().getHeight() + 5);
        pane.getChildren().add(imgView);
        pane.setBorder(new Border(new BorderStroke(Color.valueOf("#000000"),
          BorderStrokeStyle.SOLID,
          CornerRadii.EMPTY,
          BorderWidths.DEFAULT)));

        this.add(pane, 0, 0);
      });
    });
  }

  public void setAsGifDisplay(String gifId) {
    this.getChildren().clear();

    String displayUrl = displayUrlPrefix + gifId + displayUrllPostFix;
    MediaPlayer player = MediaHelper.createGifPlayer(displayUrl);
    this.mediaView = new MediaView(player);
    this.mediaView.setFitWidth(maxWidthMedia);
    this.mediaView.setCursor(Cursor.HAND);
    this.mediaView.setOnMouseClicked(e -> {
      String url = "https://media.giphy.com/media/" + gifId + "/giphy.gif";
      App._instance.getHostServices().showDocument(url);
    });
    this.add(mediaView, 0, 0);
    this.mediaView.getMediaPlayer().play();
  }

  public void setAsFileDisplay(String filename, String cdnName) {
    this.text_flow.getChildren().clear();
    this.fileView = new Hyperlink();
    fileView.setText(filename);
    fileView.setStyle("-fx-text-fill: #7A0086;");
    this.fileView.setOnMouseClicked(e -> {
      String url = App.cdnUrl + "cdn/" + cdnName;
      App._instance.getHostServices().showDocument(url);
    });
    this.fileView.setPadding(new Insets(8, 8, 8, 8));
    this.text_flow.getChildren().add(this.fileView);
  }

  public void setAsSend() {
    this.setAlignment(Pos.BASELINE_RIGHT);
    text_flow.getStyleClass().clear();
    text_flow.getStyleClass().add("msg-text-send");
  }

  public void setAsReceive() {
    this.setAlignment(Pos.BASELINE_LEFT);
    text_flow.getStyleClass().clear();
    text_flow.getStyleClass().add("msg-text-receive");
  }

  public void setText(String text) {
    text_label.setText(text);
  }
}
