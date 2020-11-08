package application.views;

import application.App;
import application.models.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Main extends BorderPane {

    @FXML
    VBox main_left_vbox;
    @FXML
    VBox pages_vbox;
    @FXML
    StackPane main_center_stackpane;
    @FXML
    UserImg user_img;
    @FXML
    Label username;
    @FXML
    Label signature;

    HomePage homePage;
    NotificationPage notificationPage;
    MessagePage messagePage;
    ProfilePage profilePage;

    @FXML
    Button post_btn;

    public Main(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
        this.getStylesheets().add(getClass().getResource("/styles/main_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        PageTab tab1 = new PageTab();
        tab1.setText("Home");
        tab1.setImage("/images/home-icon.png");
        pages_vbox.getChildren().add(tab1);
        homePage = new HomePage();
        main_center_stackpane.getChildren().add(homePage);
        tab1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openTab(1);
            }
        });

        PageTab tab2 = new PageTab();
        tab2.setText("Notifications");
        tab2.setImage("/images/notification-icon.png");
        pages_vbox.getChildren().add(tab2);
        notificationPage = new NotificationPage();
        main_center_stackpane.getChildren().add(notificationPage);
        tab2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openTab(2);
            }
        });

        PageTab tab3 = new PageTab();
        tab3.setText("Messages");
        tab3.setImage("/images/message-icon.png");
        pages_vbox.getChildren().add(tab3);
        messagePage = MessagePage.getInstance();
        main_center_stackpane.getChildren().add(messagePage);
        tab3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openTab(3);
            }
        });

        PageTab tab4 = new PageTab();
        tab4.setText("Profile");
        tab4.setImage("/images/profile-icon.png");
        pages_vbox.getChildren().add(tab4);
        profilePage = new ProfilePage();
        main_center_stackpane.getChildren().add(profilePage);
        tab4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openTab(4);
            }
        });

        openTab(1);
        loadUser();
    }

    void loadUser(){
        User user = App._userInstance.getUser();

        user_img.load("/images/user-avatar.png",20);
        //username.setText(user.getUserName());
        signature.setText("acbxyz");
    }

    void openTab(int tab){
        homePage.setVisible(false);
        notificationPage.setVisible(false);
        messagePage.setVisible(false);
        profilePage.setVisible(false);
        switch (tab){
            case 1: homePage.setVisible(true); break;
            case 2: notificationPage.setVisible(true); break;
            case 3: messagePage.setVisible(true);; break;
            case 4: profilePage.setVisible(true); break;
            default: break;
        }
    }
}
