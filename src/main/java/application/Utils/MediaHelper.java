package application.Utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaHelper {
  public static Image createImage(String url) {
    // You have to set an User-Agent in case you get HTTP Error 403
    // respond while you trying to get the Image from URL.
    
    URLConnection conn = null;
    try {
      conn = new URL(url).openConnection();
      conn.addRequestProperty("User-Agent", Contraint.UserAgent);

    } catch(Exception e) { throw new RuntimeException(e);}

    try (InputStream stream = conn.getInputStream()) {
      return new Image(stream);
    } catch(Exception e) { throw new RuntimeException(e);}
  }

  public static MediaPlayer createGifPlayer(String url) {
    Media media = new Media(url);
    MediaPlayer player = new MediaPlayer(media);

    player.setOnEndOfMedia(() -> {
      player.seek(Duration.ZERO);
    });

    return player;
  }
}
