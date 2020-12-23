package application;

import application.controllers.ConversationController;
import application.controllers.MessageController;
import application.controllers.UserController;
import application.views.AppScene;
import application.views.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends javafx.application.Application {

    static public String apiUrl = "http://larryjason.com:8081/api/";
    static public String messageSocketUrl = "ws://larryjason.com:8081/socket-service/";
    // static public String apiUrl = "http://localhost:8002/api/";
    // static public String messageSocketUrl = "ws://localhost:8002/socket-service/";

    static final public UserController _userInstance = new UserController();
    static final public MessageController _messageInstace = new MessageController();
    static final public ConversationController _conversationInstance = new ConversationController();

    @Override
    public void start(Stage primaryStage) throws Exception{

        AppScene app = new AppScene();
        Scene scene = new Scene(app,1280,720);

        primaryStage.setTitle("App");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
