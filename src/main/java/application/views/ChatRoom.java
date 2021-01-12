package application.views;

import application.App;
import application.models.Message;
import application.models.MessageFileType;
import application.models.User;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    addOnPanel.managedProperty().bind(addOnPanel.visibleProperty());
    addOnPanel.setVisible(false);
    
    chat_scrollpane.setContent(chat_messages_vbox);
    chat_scrollpane.fitToHeightProperty().set(true);

    chat_messages_vbox.setOnScroll(event -> {
      // speed up scroll speed
      double deltaY = event.getDeltaY() * 0.2; 
      double width = chat_scrollpane.getContent().getBoundsInLocal().getWidth();
      double vvalue = chat_scrollpane.getVvalue();
      chat_scrollpane.setVvalue(vvalue + -deltaY/width); 

      // scroll to top
      if (chat_scrollpane.getVvalue() <= 0.001) {
        if (!isLoading) {
          this.isLoading = true;
          App.executor.execute(() -> {
            try {
              App._messageInstace.loadMessasgeFromConversation(name);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
        }
      }
    });

    // ======================

    send_rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
          sendMessage();
        }
      }
    });

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
    chat_scrollpane.setVvalue(1.0);
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

    if (messageIndex <= 15) {
      scrollToBottom();
    }
  }

  public void addLastMessage(Message message) {
    User user = App._userInstance.getUser();
    MessageText msg = new MessageText();

    if (message.getSender().equals(user.getUserName())) {
      msg.setAsSend();
      msg.setText(message.getContent());
      chat_messages_vbox.getChildren().add(msg);
    } else {
      msg.setAsReceive();
      msg.setText(message.getContent());
      chat_messages_vbox.getChildren().add(msg);
    }
    scrollToBottom();
  }
}
