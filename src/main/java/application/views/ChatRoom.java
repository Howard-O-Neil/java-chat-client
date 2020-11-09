package application.views;

import application.App;
import application.models.Message;
import application.models.User;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    Rectangle send_rect;
    @FXML
    ScrollPane chat_scrollpane;
    @FXML
    VBox chat_messages_vbox;

    int messageIndex = 0;
    int messageCount = 0;

    public ChatRoom(String name){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chatroom.fxml"));
        this.getStylesheets().add(App.class.getResource("/styles/chatroom_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        send_rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    sendMessage();
                }
            }
        });

        loadResource();
        chatroom_name_label.setText(name);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    App._messageInstace.loadMessasgeFromConversation(name);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            goToBottomScrollPane();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getMessageIndex() { return messageIndex; }
    public void setMessageIndex(int i) { messageIndex = i;}

    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int i) { messageCount = i;}

    void loadResource(){
        Image settingImg = new Image("/images/setting-icon.png");
        setting_rect.setFill(new ImagePattern(settingImg));

        Image attachmentImg = new Image("/images/attachment-icon.png");
        attachment_rect.setFill(new ImagePattern(attachmentImg));

        Image sendImg = new Image("/images/send-icon.png");
        send_rect.setFill(new ImagePattern(sendImg));

        // sample text msg

    }

    void sendMessage(){
        if(text_area.getText().isEmpty()) return;
        System.out.println("msg send");

        new Thread(new Runnable() {
            @Override
            public void run() {
                App._messageInstace.sendMessage(chatroom_name_label.getText(), text_area.getText());
                text_area.setText("");
            }
        }).start();
    }

    public void addMessage(Message message){
        User user = App._userInstance.getUser();
        MessageText msg = new MessageText();

        if(message.getSender().equals(user.getUserName())){
            msg.setAsSend();
            msg.setText(message.getContent());
            chat_messages_vbox.getChildren().add(msg);
        }
        else{
            msg.setAsReceive();
            msg.setText(message.getContent());
            chat_messages_vbox.getChildren().add(msg);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                goToBottomScrollPane();
            }
        });
    }

    public void goToBottomScrollPane(){
        chat_scrollpane.setVvalue(0.9f);
        chat_scrollpane.setVvalue(1.0f);
    }
}
