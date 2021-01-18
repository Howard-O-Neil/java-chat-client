package application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.controllers.ConversationController;
import application.controllers.GifController;
import application.controllers.MessageController;
import application.controllers.UserController;
import application.views.AppScene;
import application.views.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends javafx.application.Application {

  static public final String giphyApiKey = "0L5XCnJwI21DzramGOLEE5sZiUDihjVe";
  // public static String apiUrl = "http://larryjason.com:8081/api/";
  // public static String messageSocketUrl ="ws://larryjason.com:8081/socket-service/";
  static public String cdnUrl = "http://localhost:8082/";
  static public String apiUrl = "http://localhost:8081/api/";
  static public String messageSocketUrl = "ws://localhost:8081/socket-service/";

  static public App _instance = null;

  public static final UserController _userInstance = new UserController();
  public static final MessageController _messageInstace = new MessageController();
  public static final ConversationController _conversationInstance = new ConversationController();
  public static final GifController _gifInstance = new GifController();
  public static final ExecutorService executor = Executors.newFixedThreadPool(10);
  public static Stage _primaryStage = null;

  @Override
  public void start(Stage primaryStage) throws Exception {
    _instance = this;
    
    AppScene app = new AppScene();
    Scene scene = new Scene(app, 1280, 720);

    primaryStage.setOnCloseRequest(e -> {
      App.executor.shutdownNow();

      if (_messageInstace.session != null)
        _messageInstace.session.disconnect();
      
      if (_conversationInstance.session != null)
        _conversationInstance.session.disconnect();
    });

    primaryStage.setTitle("App");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();

    _primaryStage = primaryStage;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
