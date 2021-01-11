package application.views;

import application.App;
import application.models.Conversation;
import java.io.IOException;
import java.util.concurrent.FutureTask;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MessagePage extends BorderPane {

  @FXML
  SearchBar searchbar;

  @FXML
  Button start_conversation_button;

  @FXML
  ScrollPane conversation_scrollPane;

  @FXML
  VBox conversation_vbox;

  int conversationIndex = 0;

  public MessagePage() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/messagepage.fxml"));
    this.getStylesheets().add(getClass().getResource("/styles/messagepage_style.css").toExternalForm());
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    managedProperty().bind(visibleProperty());

    conversation_scrollPane.setContent(conversation_vbox);

    BooleanProperty firstTime = new SimpleBooleanProperty(true);

    searchbar.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue && firstTime.get()) {
        this.requestFocus(); // Delegate the focus to container
        firstTime.setValue(false); // Variable value changed for future references
      }
    });

    start_conversation_button.setOnAction(actionEvent -> {
      App.executor.execute(() -> {
        try {
          App._conversationInstance.addNewConversation(searchbar.getText());
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    });

    App.executor.execute(() -> {
      try {
        App._conversationInstance.loadConversation();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    App.executor.execute(() -> {
      try {
        App._messageInstace.loadMessage();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public static MessagePage _instance;

  public static MessagePage getInstance() {
    if (_instance == null)
      _instance = new MessagePage();
    return _instance;
  }

  public int getConversationIndex() {
    return conversationIndex;
  }

  public void setConversationIndex(int i) {
    conversationIndex = i;
  }

  public void addConversation(Conversation conversation) {
    ConversationCell cell = new ConversationCell();
    cell.setUsername(conversation.getReceiver());

    App.executor.execute(() -> {
      try {
        App._userInstance.requestConversationSignature(conversation.getReceiver(), cell);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    cell.setOnMouseClicked(mouseEvent -> {
      App.executor.execute(() -> {
        try {
          App._messageInstace.loadFromConversation(conversation.getReceiver());
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    });
    conversation_vbox.getChildren().add(0, cell);
  }

  ChatRoom currentRoom;

  public ChatRoom getRoom() {
    return currentRoom;
  }

  public void openChatRoom(String name) {
    ChatRoom room = new ChatRoom(name);
    currentRoom = room;
    this.setCenter(room);
  }
}
