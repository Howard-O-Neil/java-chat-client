package application.views;

import application.App;
import application.Utils.FormHelper;
import application.Utils.TimerHelper;
import application.models.Message;
import application.models.MessageFileType;
import application.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class ChatRoom extends BorderPane {

  @FXML
  Label chatroom_name_label;
  @FXML
  TextArea text_area;
  @FXML
  Rectangle setting_rect;
  @FXML
  Rectangle attachment_rect;
  @FXML
  Rectangle imageBtn;
  @FXML
  Rectangle gifBtn;
  @FXML
  Rectangle fileBtn;
  @FXML
  HBox addOnPanel;
  @FXML
  Rectangle send_rect;
  @FXML
  ScrollPane chat_scrollpane;
  @FXML
  VBox scrollpane_content;
  @FXML
  Hyperlink chat_messages_loadmore;
  @FXML
  VBox chat_messages_vbox;

  int messageIndex = 0;
  int messageCount = 0;
  boolean isLoading = false;

  public ChatRoom(String name) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chatroom.fxml"));
    this.getStylesheets().add(App.class.getResource("/styles/chatroom_style.css").toExternalForm());
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    // set property
    chat_messages_loadmore.managedProperty().bind(chat_messages_loadmore.visibleProperty());
    addOnPanel.managedProperty().bind(addOnPanel.visibleProperty());
    addOnPanel.setVisible(false);

    attachment_rect.setOnMouseClicked(e -> {
      addOnPanel.setVisible(!addOnPanel.isVisible());
    });

    chat_messages_loadmore.setOnMouseClicked(e -> {
      try {FormHelper.test();}
      catch (Exception ex) {ex.printStackTrace();}

      if (!isLoading) {
        this.isLoading = true;
        App.executor.execute(() -> {
          try {
            App._messageInstace.loadMessasgeFromConversation(name);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
      }
    });
    
    chat_scrollpane.setContent(scrollpane_content);
    // chat_scrollpane.vvalueProperty().bind(chat_scrollpane.heightProperty());
    // ======================

    send_rect.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
        sendMessage();
      }});

    loadResource();
    chatroom_name_label.setText(name);

    App.executor.execute(() -> {
      try {
        App._messageInstace.loadMessasgeFromConversation(name);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public void setIsLoading(boolean value) {
    this.isLoading = value;
  }

  public int getMessageIndex() {
    return messageIndex;
  }

  public void setMessageIndex(int i) {
    messageIndex = i;
  }

  public void scrollToBottom() {
    chat_scrollpane.setVvalue(1.0d);
  }

  public void setLoadMoreVisibility(boolean val) {
    chat_messages_loadmore.visibleProperty().set(val);
  }

  public void scrollDown() {
    double increment = (50 / scrollpane_content.getHeight());
    chat_scrollpane.setVvalue(chat_scrollpane.getVvalue() + increment);
  }

  public int getMessageCount() {
    return messageCount;
  }

  public void setMessageCount(int i) {
    messageCount = i;
  }

  void loadResource() {
    Image settingImg = new Image("/images/setting-icon.png");
    setting_rect.setFill(new ImagePattern(settingImg));

    Image attachmentImg = new Image("/images/attachment-icon.png");
    attachment_rect.setFill(new ImagePattern(attachmentImg));

    Image sendImg = new Image("/images/send-icon.png");
    send_rect.setFill(new ImagePattern(sendImg));

    Image gifIcon = new Image("/images/gif-icon.png");
    gifBtn.setFill(new ImagePattern(gifIcon));

    gifBtn.setOnMouseClicked(event -> {
      System.out.println("fuck");
      Main.getInstance().getChildren().add(new GifPicker());
    });

    Image imgIcon = new Image("/images/image-icon.png");
    imageBtn.setFill(new ImagePattern(imgIcon));

    Image fileIcon = new Image("/images/file-icon.png");
    fileBtn.setFill(new ImagePattern(fileIcon));
    // sample text msg
  }

  void sendMessage() {
    if (text_area.getText().isEmpty())
      return;

    App.executor.execute(() -> {
      App._messageInstace.sendMessage(chatroom_name_label.getText(), 
        text_area.getText(), MessageFileType.NONE, "");
      text_area.setText("");
    });
  }

  public void addMessage(Message message) {
    User user = App._userInstance.getUser();
    MessageText msg = new MessageText();

    if (message.getSender().equals(user.getUserName())) {
      msg.setAsSend();
      msg.setText(message.getContent());
      chat_messages_vbox.getChildren().add(0, msg);
    } else {
      msg.setAsReceive();
      msg.setText(message.getContent());
      chat_messages_vbox.getChildren().add(0, msg);
    }
    scrollDown();
  }

  public void addLastMessage(Message message) {
    User user = App._userInstance.getUser();
    MessageText msg = new MessageText();

    if (message.getSender().equals(user.getUserName())) {
      msg.setAsSend();
      msg.setText(message.getContent());
      chat_messages_vbox.getChildren().add(msg);

      TimerHelper.executeOnceAfter(() -> {
        scrollToBottom();
      }, 200);
    } else {
      msg.setAsReceive();
      msg.setText(message.getContent());
      chat_messages_vbox.getChildren().add(msg);
    }
  }
}
