package application.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class PageTab extends HBox {

    @FXML
    Rectangle imagerect;
    @FXML
    Label label;

    public PageTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/pagetab.fxml"));
        this.getStylesheets().add(getClass().getResource("/styles/pagetab_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setImage(String url){
        Image img = new Image(url);
        imagerect.setFill(new ImagePattern(img));
    }

    public void setText(String text){
        label.setText(text);
    }
}
