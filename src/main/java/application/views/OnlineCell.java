package application.views;

import application.Main;
import application.models.Conversation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class OnlineCell extends VBox {

    @FXML
    Label name_label;
    @FXML
    Circle status_dot;
    @FXML
    Circle user_icon;

    Conversation conversation;

    public OnlineCell(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/onlinecell.fxml"));
        this.getStylesheets().add(Main.class.getResource("/styles/onlinecell_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setUsername(String name){
        name_label.setText(name);
    }
    public String getUsername() { return name_label.getText(); }

    public void setUserImage(String url){
        Image img = new Image(url);
        user_icon.setFill(new ImagePattern(img));
    }

    public void isOnline(Boolean online){
        if(online)
            status_dot.setFill(Color.GREEN);
        else
            status_dot.setFill(Color.GRAY);
    }

    public void makeConversation(){
        conversation = new Conversation(
                Main._user.getUserName(),
                name_label.getText()
        );
    }

    public Conversation getConversation(){return  conversation;}
}
