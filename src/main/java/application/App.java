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

  // public static String apiUrl = "http://larryjason.com:8081/api/";
  // public static String messageSocketUrl ="ws://larryjason.com:8081/socket-service/";
  static public String apiUrl = "http://localhost:8081/api/";
  static public String messageSocketUrl = "ws://localhost:8081/socket-service/";

  public static final UserController _userInstance = new UserController();
  public static final MessageController _messageInstace = new MessageController();
  public static final ConversationController _conversationInstance = new ConversationController();

  @Override
  public void start(Stage primaryStage) throws Exception {
    AppScene app = new AppScene();
    Scene scene = new Scene(app, 1280, 720);

    primaryStage.setTitle("App");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
