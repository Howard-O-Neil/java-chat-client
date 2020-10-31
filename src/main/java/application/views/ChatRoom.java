package application.views;

import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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
    VBox chat_messages_vbox;

    public ChatRoom(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chatroom.fxml"));
        this.getStylesheets().add(Main.class.getResource("/styles/chatroom_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        loadResource();

        send_rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    sendMessage();
                }
            }
        });
    }

    void loadResource(){
        Image settingImg = new Image("/images/setting-icon.png");
        setting_rect.setFill(new ImagePattern(settingImg));

        Image attachmentImg = new Image("/images/attachment-icon.png");
        attachment_rect.setFill(new ImagePattern(attachmentImg));

        Image sendImg = new Image("/images/send-icon.png");
        send_rect.setFill(new ImagePattern(sendImg));

        MessageText msg1 = new MessageText();
        msg1.setAsSend();
        msg1.setText("Hello this is send msg, that is too long to display so that it need wrap text to be true");
        chat_messages_vbox.getChildren().add(msg1);
        MessageText msg2 = new MessageText();
        msg2.setAsReceive();
        msg2.setText("Hello this is send msg");
        chat_messages_vbox.getChildren().add(msg2);
    }

    void sendMessage(){
        if(text_area.getText().isEmpty()) return;
        System.out.println("msg send");
        text_area.setText("");
    }

    public void setChatName(String name){ chatroom_name_label.setText(name);}
}
