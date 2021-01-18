package application.views;

import java.util.regex.Pattern;

import application.App;
import application.Utils.MediaHelper;
import application.models.Giphy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class GifPicker extends BorderPane {
  @FXML
  ScrollPane gif_scrollPane;

  @FXML
  VBox gif_content;

  @FXML
  VBox gif_scrollPane_content;

  @FXML
  Hyperlink gif_load_more;

  @FXML
  TextField gif_search_txt;

  @FXML
  Button gif_search;

  private int offset = 0;
  private String mode = "trending";

  private String searchString = "";
  private final String displayUrlPrefix = "https://i.giphy.com/";
  private final String displayUrllPostFix = "/giphy.mp4";

  GifPicker() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/gifPicker.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    gif_scrollPane.setContent(gif_scrollPane_content);
    gif_load_more.setOnMouseClicked(e -> {
      App._gifInstance.loadGif();
    });
    gif_search_txt.textProperty().addListener(listener -> {
      if (Pattern.compile("[^a-zA-Z0-9]").matcher(gif_search_txt.getText()).matches()) {
        gif_search_txt.setText(gif_search_txt.getText().replaceAll("[^a-zA-Z0-9]", ""));
      }
      searchString = gif_search_txt.getText();
    });
    gif_search.setOnMouseClicked(event -> {
      offset = 0;
      gif_content.getChildren().clear();

      if (searchString.length() > 0) {
        mode = "searching";
      } else {
        mode = "trending";
      }
      App._gifInstance.loadGif();
    });
    offset = 0;
  }

  public void addGif(Giphy.Root gif) {
    String displayUrl = displayUrlPrefix + gif.id + displayUrllPostFix;

    System.out.println(displayUrl);

    App.executor.execute(() -> {
      MediaPlayer player = MediaHelper.createGifPlayer(displayUrl);
      MediaView view = new MediaView(player);
      view.fitWidthProperty().set(343);
      view.setCursor(Cursor.HAND);

      view.setOnMouseClicked(event -> {
        MessagePage.getInstance().getRoom().sendGif(gif.id);
      });

      Platform.runLater(() -> {
        gif_content.getChildren().add(view);
        view.getMediaPlayer().play();
      });
    });
  }

  // public void addGif(Giphy.Root gif) {
  //   String displayUrl = displayUrlPrefix + gif.id + displayUrllPostFix;

  //   System.out.println(displayUrl);

  //   App.executor.execute(() -> {
  //     Image img = MediaHelper.createImage(displayUrl);
  //     ImageView view = new ImageView(img);
  //     view.fitWidthProperty().set(343);

  //     Platform.runLater(() -> {
  //       gif_content.getChildren().add(view);
  //     });
  //   });
  // }

  // public void addGif(Giphy.Root gif) {
  //   String displayUrl = displayUrlPrefix + gif.id + displayUrllPostFix;

  //   System.out.println(displayUrl);

  //   App.executor.execute(() -> {
  //     WebView webView = new WebView();
  //     webView.getEngine().loadContent(displayUrl);
  //     webView.setMaxWidth(343);
  //     webView.setPrefWidth(343);

  //     Platform.runLater(() -> {
  //       gif_content.getChildren().add(webView);
  //     });
  //   });
  // }

  public int getCurrentOffset() {
    return offset;
  }

  public String getSearchString() {
    return searchString.replaceAll("\\s+","-");
  }

  public void addCurrentOffset(int val) {
    offset += val;
  }

  public String getMode() {
    return mode;
  }
}
