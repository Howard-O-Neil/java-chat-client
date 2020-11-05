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
    static public String messageSocketUrl = "http://larryjason.com:8081/";

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

    @Override
    public void stop() throws Exception {
        super.stop();
        _conversationInstance.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
