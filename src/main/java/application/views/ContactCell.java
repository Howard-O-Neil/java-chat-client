package application.views;

import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class ContactCell extends HBox{

    @FXML
    Label contact_name_label;
    @FXML
    Label signature_label;

    @FXML
    Circle status_dot;
    @FXML
    Circle user_icon;

    public ContactCell(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/contactcell.fxml"));
        this.getStylesheets().add(Main.class.getResource("/styles/contactcell_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("Double clicked");
                        openChatRoom();
                    }
                }
                if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                    System.out.println("M2 clicked");
                }
            }
        });
    }

    public void setUsername(String name){
        contact_name_label.setText(name);
    }

    public void setUserImage(String url){
        Image img = new Image(url);
        user_icon.setFill(new ImagePattern(img));
    }

    public void setSignature(String signature){ signature_label.setText(signature); }

    public void isOnline(Boolean online){
        if(online)
            status_dot.setFill(Color.GREEN);
        else
            status_dot.setFill(Color.GRAY);
    }

    Parent main_node;
    public void setParentScene(Parent main){
        main_node = main;
    }

    void openChatRoom(){
        ChatRoom chatroom = new ChatRoom();
        BorderPane scene = ((BorderPane)main_node);
        scene.setCenter(chatroom);
    }
}
