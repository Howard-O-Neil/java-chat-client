package application.controllers;

import application.Main;
import application.views.ChatRoom;
import application.views.ContactCell;
import application.views.OnlineCell;
import application.views.SearchBar;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    BorderPane main_layout;
    @FXML
    Circle user_avatar_img;
    @FXML
    Label username_label;
    @FXML
    Label signature_label;
    @FXML
    SearchBar search_textfield;
    @FXML
    Button add_conversation_button;
    @FXML
    ScrollPane online_contacts_scrollpane;
    @FXML
    ScrollPane recent_contacts_scrollpane;
    @FXML
    HBox online_contacts_hbox;
    @FXML
    VBox recent_contacts_vbox;
    @FXML
    Circle add_circle;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        add_circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){

                    }
                }
            }
        });

        add_conversation_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addConversion();
            }
        });

        loadResource();
        loadUserInfo();
        loadOnlineUser();
        loadUserContact();
    }

    void loadResource(){
        Image addImg = new Image("/images/add-icon.png");
        add_circle.setFill(new ImagePattern(addImg));
    }

    void loadUserInfo(){
        username_label.setText(Main._user.getUserName());
        Image userImg = new Image("/images/user-avatar.png");
        user_avatar_img.setFill(new ImagePattern(userImg));
    }

    void loadUserContact(){
        ObservableList onlineList = online_contacts_hbox.getChildren();
    }

    void loadOnlineUser(){
        ObservableList recentList = recent_contacts_vbox.getChildren();
    }

    OnlineCell newOnlineCell(String username, String imageUrl, Boolean online){
        OnlineCell cell = new OnlineCell();
        cell.setUserImage(imageUrl);
        cell.setUsername(username);
        cell.isOnline(online);
        cell.makeConversation();
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2){
                    Main._conversationInstance.loadConversation();
                    openChatRoom(cell.getUsername());
                }
            }
        });
        return cell;
    }

    ContactCell newContactCell(String username,String signature, String imageUrl, Boolean online){
        ContactCell cell = new ContactCell();
        cell.setUserImage(imageUrl);
        cell.setUsername(username);
        cell.setSignature(signature);
        cell.isOnline(online);
        cell.setParentScene(main_layout);
        return cell;
    }

    void openChatRoom(String chatroomname){
        ChatRoom chatroom = new ChatRoom();
        chatroom.setChatName(chatroomname);
        main_layout.setCenter(chatroom);
    }

    void addConversion(){

        ObservableList onlineList = online_contacts_hbox.getChildren();
        OnlineCell cell = newOnlineCell(search_textfield.getText(),"/images/user-avatar.png",true);
        try{
            Boolean available = Main._conversationInstance.addNewConversation(cell.getConversation());
            if(available) {
                onlineList.add(cell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
