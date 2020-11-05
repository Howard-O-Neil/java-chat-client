package application.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class NotificationPage extends StackPane {
    public NotificationPage(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/notificationpage.fxml"));
        //this.getStylesheets().add(getClass().getResource("/styles/main_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        managedProperty().bind(visibleProperty());
    }
}
