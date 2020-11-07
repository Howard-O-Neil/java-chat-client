package application.views;

import application.App;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MessagePage extends BorderPane {

    @FXML
    SearchBar searchbar;
    @FXML
    Button start_conversation_button;
    @FXML
    VBox conversation_vbox;

    public MessagePage(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/messagepage.fxml"));
        this.getStylesheets().add(getClass().getResource("/styles/messagepage_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        managedProperty().bind(visibleProperty());

        BooleanProperty firstTime = new SimpleBooleanProperty(true);
        searchbar.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                this.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        start_conversation_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addConversation("ABC","xyz");
            }
        });
        App._conversationInstance.loadConversation();
    }

    public void addConversation(String username, String signature){
        ConversationCell cell = new ConversationCell();
        cell.setUsername(username);
        cell.setSignature(signature);
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openChat();
            }
        });
        conversation_vbox.getChildren().add(cell);
    }

    void openChat(){
        ChatRoom room = new ChatRoom();
        this.setCenter(room);
    }
}
