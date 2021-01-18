package application.views;

import application.App;
import application.Utils.FormHelper;
import application.Utils.TimerHelper;
import application.models.Message;
import application.models.MessageFileType;
import application.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

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

  GifPicker gifPicker = null;

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
    Main.getInstance().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
      if (gifPicker != null) {
        if (!inHierarchy(evt.getPickResult().getIntersectedNode(), gifPicker)) {
          disolveGifPicker();
        }
      }
    });

    chat_messages_loadmore.managedProperty().bind(chat_messages_loadmore.visibleProperty());
    addOnPanel.managedProperty().bind(addOnPanel.visibleProperty());
    addOnPanel.setVisible(false);

    attachment_rect.setOnMouseClicked(e -> {
      addOnPanel.setVisible(!addOnPanel.isVisible());

      Image attachmentImg = new Image("/images/attachment-icon.png");
      Image cancelImg = new Image("/images/cancel-icon.png");

      if (addOnPanel.isVisible()) {
        attachment_rect.setFill(new ImagePattern(cancelImg));
      } else {
        attachment_rect.setFill(new ImagePattern(attachmentImg));
      }
    });

    chat_messages_loadmore.setOnMouseClicked(e -> {
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

  public GifPicker getGifPicker() {
    return gifPicker;
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
      createGifPicker();
    });

    Image imgIcon = new Image("/images/image-icon.png");
    imageBtn.setFill(new ImagePattern(imgIcon));
    imageBtn.setOnMouseClicked(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png", "*.png"),
          new FileChooser.ExtensionFilter("jpg", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.PNG"),
          new FileChooser.ExtensionFilter("JPG", "*.JPG"));

      File selectedFile = fileChooser.showOpenDialog(App._primaryStage);

      try {
        FormHelper.submitData(selectedFile, httpResponse -> {
          HttpEntity entity = httpResponse.getEntity();
          String responseBody = entity != null ? EntityUtils.toString(entity) : null;

          sendImage(responseBody.split("\"")[3]);
        });
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });

    Image fileIcon = new Image("/images/file-icon.png");
    fileBtn.setFill(new ImagePattern(fileIcon));
    fileBtn.setOnMouseClicked(event -> {
      FileChooser fileChooser = new FileChooser();
      File selectedFile = fileChooser.showOpenDialog(App._primaryStage);

      try {
        FormHelper.submitData(selectedFile, httpResponse -> {
          HttpEntity entity = httpResponse.getEntity();
          String responseBody = entity != null ? EntityUtils.toString(entity) : null;

          sendFile(selectedFile.getName(), responseBody.split("\"")[3]);
        });
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });
    // sample text msg
  }

  public static boolean inHierarchy(Node node, Node potentialHierarchyElement) {
    if (potentialHierarchyElement == null) {
      return true;
    }
    while (node != null) {
      if (node == potentialHierarchyElement) {
        return true;
      }
      node = node.getParent();
    }
    return false;
  }


  void createGifPicker() {
    if (this.gifPicker != null) {
      disolveGifPicker();
      return;
    }
    
    this.gifPicker = new GifPicker();
    gifPicker.setLayoutX(gifBtn.getLayoutX() + 400);
    gifPicker.setLayoutY(gifBtn.getLayoutY() + 100);

    Main.getInstance().getChildren().add(gifPicker);

    App._gifInstance.loadGif();
    // Main.getInstance().setDisable(true);
  }

  void disolveGifPicker() {
    if (gifPicker == null)
      return;

    Main.getInstance().getChildren().remove(gifPicker);
    gifPicker = null;
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

  void sendImage(String fileName) {
    App.executor.execute(() -> {
      App._messageInstace.sendMessage(chatroom_name_label.getText(), 
        "", MessageFileType.IMAGE, fileName);
    });
  }

  void sendGif(String gifId) {
    App.executor.execute(() -> {
      App._messageInstace.sendMessage(chatroom_name_label.getText(), 
        "", MessageFileType.GIF, gifId);
    });
  }

  void sendFile(String filename, String cdnName) {
    App.executor.execute(() -> {
      App._messageInstace.sendMessage(chatroom_name_label.getText(), 
        filename, MessageFileType.FILE, cdnName);
    });
  }

  private void updateFileType(MessageText msg, Message message) {
    if (message.getFileType().equals(MessageFileType.GIF.toString())) {
      msg.setAsGifDisplay(message.getFileContent());
    } else if (message.getFileType().equals(MessageFileType.IMAGE.toString())) {
      msg.setAsImageDisplay(message.getFileContent());
    } else if (message.getFileType().equals(MessageFileType.FILE.toString())) {
      msg.setAsFileDisplay(message.getContent(), message.getFileContent());
    } else {
      msg.setText(message.getContent());
    }
  }

  public void addMessage(Message message) {
    User user = App._userInstance.getUser();
    MessageText msg = new MessageText();

    if (message.getSender().equals(user.getUserName())) {
      msg.setAsSend();
      updateFileType(msg, message);
      chat_messages_vbox.getChildren().add(0, msg);
    } else {
      msg.setAsReceive();
      updateFileType(msg, message);
      chat_messages_vbox.getChildren().add(0, msg);
    }
    scrollDown();
  }

  public void addLastMessage(Message message) {
    User user = App._userInstance.getUser();
    MessageText msg = new MessageText();

    if (message.getSender().equals(user.getUserName())) {
      msg.setAsSend();
      updateFileType(msg, message);
      chat_messages_vbox.getChildren().add(msg);

      TimerHelper.executeOnceAfter(() -> {
        scrollToBottom();
      }, 200);
    } else {
      msg.setAsReceive();
      updateFileType(msg, message);
      chat_messages_vbox.getChildren().add(msg);
    }
  }
}
