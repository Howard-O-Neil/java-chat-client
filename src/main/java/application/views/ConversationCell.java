package application.views;

import java.io.IOException;

import application.App;
import application.controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ConversationCell extends GridPane {

  @FXML
  UserImg user_img;

  @FXML
  Label username;

  @FXML
  Label signature;

  public ConversationCell() {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/views/conversationcell.fxml")
    );
    this.getStylesheets()
      .add(
        getClass()
          .getResource("/styles/conversationcell_style.css")
          .toExternalForm()
      );
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    user_img.load("/images/user-avatar.png", 16);
  }

  public void setUsername(String name) {
    username.setText(name);
  }

  public void setSignature(String sign) {
    signature.setText(sign);
  }
}
