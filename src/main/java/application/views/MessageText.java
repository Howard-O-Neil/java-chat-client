package application.views;

import application.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class MessageText extends GridPane {

    @FXML
    Label text_label;
    @FXML
    Circle contact_icon;

    public MessageText(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/messagetext.fxml"));
        this.getStylesheets().add(App.class.getResource("/styles/messagetext_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        contact_icon.managedProperty().bind(contact_icon.visibleProperty());
    }

    public void setAsSend(){
        this.setAlignment(Pos.BASELINE_RIGHT);
        text_label.getStyleClass().clear();
        text_label.getStyleClass().add("msg-text-send");
        contact_icon.setVisible(false);
    }

    public void setAsReceive(){
        this.setAlignment(Pos.BASELINE_LEFT);
        text_label.getStyleClass().clear();
        text_label.getStyleClass().add("msg-text-receive");
        contact_icon.setVisible(true);
    }

    public void setText(String text){
        text_label.setText(text);
    }
}
