package application.views;

import application.App;
import application.controllers.AlertDialog;
import application.models.Response;
import application.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

public class Login extends StackPane {

  class LoginAuthThread extends Thread {

    @Override
    public void run() {
      try {
        login();
      } catch (Exception e) {
        Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              ConnectError();
              progessing = false;
            }
          }
        );
        e.printStackTrace();
      }
    }

    Runnable failed = new Runnable() {
      @Override
      public void run() {
        AlertDialog.makeAler(
          "Login failed",
          "Wrong username or password",
          "Please try enter a correct username and password",
          Alert.AlertType.INFORMATION
        );
      }
    };

    Runnable success = new Runnable() {
      @Override
      public void run() {
        try {
          openMainScene();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    void login() throws Exception {
      User user = new User(username.getText(), password.getText());

      CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
      try {
        client.start();

        HttpPost request = new HttpPost(App.apiUrl + "user/checklogin");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        Gson gson = new Gson();
        String json = gson.toJson(user);
        StringEntity stringEntity = new StringEntity(json);
        request.setEntity(stringEntity);

        Future<HttpResponse> future = client.execute(request, null);
        HttpResponse httpResponse = future.get();
        String responseBody;
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
          HttpEntity entity = httpResponse.getEntity();
          responseBody = entity != null ? EntityUtils.toString(entity) : null;
        } else {
          throw new ClientProtocolException(
            "Unexpected response status: " + status
          );
        }
        System.out.println("----------------------------------------");
        System.out.println(responseBody);

        if (responseBody == null || responseBody.contains("login-failed")) {
          Platform.runLater(failed);
        } else {
          Response<User> response = gson.fromJson(
            responseBody,
            new TypeToken<Response<User>>() {}.getType()
          );
          App._userInstance.setUser(response.getData());

          Platform.runLater(success);
        }
      } finally {
        client.close();
      }

      progessing = false;
    }
  }

  class SignUpThread extends Thread {

    @Override
    public void run() {
      try {
        signup();
      } catch (Exception e) {
        Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              ConnectError();
              progessing = false;
            }
          }
        );
        e.printStackTrace();
      }
    }

    Runnable failed = new Runnable() {
      @Override
      public void run() {
        AlertDialog.makeAler(
          "Sign up failed",
          "Some thing went wrong",
          "Please try again or another username/password",
          Alert.AlertType.INFORMATION
        );
      }
    };

    Runnable success = new Runnable() {
      @Override
      public void run() {
        try {
          AlertDialog.makeAler(
            "Sign up success",
            "OK",
            "You can now log in",
            Alert.AlertType.INFORMATION
          );
          login_link.fire();
          username.setText(signup_username.getText());
          password.setText(signup_password.getText());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    void signup() throws Exception {
      User user = new User(
        signup_username.getText(),
        signup_password.getText()
      );

      CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
      try {
        client.start();

        HttpPost request = new HttpPost(App.apiUrl + "user/add");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        Gson gson = new Gson();
        String json = gson.toJson(user);
        StringEntity stringEntity = new StringEntity(json);
        request.setEntity(stringEntity);

        Future<HttpResponse> future = client.execute(request, null);
        HttpResponse httpResponse = future.get();
        String responseBody;
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
          HttpEntity entity = httpResponse.getEntity();
          responseBody = entity != null ? EntityUtils.toString(entity) : null;
        } else {
          throw new ClientProtocolException(
            "Unexpected response status: " + status
          );
        }
        System.out.println("----------------------------------------");
        System.out.println(responseBody);

        if (responseBody == null || responseBody.contains("login-failed")) {
          Platform.runLater(failed);
        } else {
          Response<Boolean> response = gson.fromJson(
            responseBody,
            new TypeToken<Response<Boolean>>() {}.getType()
          );
          if (response.getData() == true) {
            Platform.runLater(success);
          } else {
            Platform.runLater(failed);
          }
        }
      } finally {
        client.close();
      }

      progessing = false;
    }
  }

  @FXML
  VBox login_layout;

  @FXML
  ImageView logo_img1;

  @FXML
  ImageView logo_img2;

  @FXML
  TextField username;

  @FXML
  PasswordField password;

  @FXML
  Label login_check_msg;

  @FXML
  Button login_button;

  @FXML
  Hyperlink signup_link;

  @FXML
  VBox signup_layout;

  @FXML
  TextField signup_username;

  @FXML
  TextField signup_email;

  @FXML
  PasswordField signup_password;

  @FXML
  PasswordField confirm_password;

  @FXML
  Label signup_check_msg;

  @FXML
  Button signup_button;

  @FXML
  Hyperlink login_link;

  boolean progessing = false;

  public Login() {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/views/login.fxml")
    );
    this.getStylesheets()
      .add(getClass().getResource("/styles/login_style.css").toExternalForm());
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    BooleanProperty firstTime = new SimpleBooleanProperty(true);
    username
      .focusedProperty()
      .addListener(
        (observable, oldValue, newValue) -> {
          if (newValue && firstTime.get()) {
            this.requestFocus(); // Delegate the focus to container
            firstTime.setValue(false); // Variable value changed for future references
          }
        }
      );

    login_layout.managedProperty().bind(login_layout.visibleProperty());
    login_layout.setVisible(true);
    signup_layout.managedProperty().bind(signup_layout.visibleProperty());
    signup_layout.setVisible(false);

    signup_link.setOnAction(
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
          if (progessing) return;
          login_layout.setVisible(false);
          signup_layout.setVisible(true);
          signup_username.clear();
          signup_email.clear();
          signup_password.clear();
          confirm_password.clear();
          signup_check_msg.setText("");
        }
      }
    );

    login_link.setOnAction(
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
          if (progessing) return;
          signup_layout.setVisible(false);
          login_layout.setVisible(true);
          username.clear();
          password.clear();
          login_check_msg.setText("");
        }
      }
    );

    login_button.setOnAction(
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
          if (!verifyLogIn()) return;
          if (!progessing) {
            progessing = true;
            LoginAuthThread thread = new LoginAuthThread();
            thread.start();
          }
        }
      }
    );

    signup_button.setOnAction(
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
          if (!verifySignUp()) return;
          if (!progessing) {
            progessing = true;
            SignUpThread thread = new SignUpThread();
            thread.start();
          }
        }
      }
    );

    loadResource();
  }

  private Boolean verifyLogIn() {
    if (username.getText().isBlank()) {
      login_check_msg.setText("username can\'t be blank");
      return false;
    }
    if (password.getText().isBlank()) {
      login_check_msg.setText("password can\'t be blank");
      return false;
    }
    login_check_msg.setText("");
    return true;
  }

  private Boolean verifySignUp() {
    if (signup_username.getText().isBlank()) {
      signup_check_msg.setText("username can\'t be blank");
      return false;
    }
    if (
      !signup_email
        .getText()
        .matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
    ) {
      signup_check_msg.setText("email is invalid");
      return false;
    }
    if (signup_password.getText().isBlank()) {
      signup_check_msg.setText("password can\'t be blank");
      return false;
    }
    if (!confirm_password.getText().contentEquals(signup_password.getText())) {
      signup_check_msg.setText("password not match");
      return false;
    }
    signup_check_msg.setText("");
    return true;
  }

  private void loadResource() {
    Image img = new Image("/images/chat-logo.png");
    logo_img1.setImage(img);
    logo_img2.setImage(img);
  }

  private void openMainScene() throws IOException {
    HBox parent = (HBox) getParent();
    parent.getChildren().clear();
    parent.getChildren().add(new Main());
  }

  private void ConnectError() {
    AlertDialog.makeAler(
      "Connection error",
      "Can\'t connect to server",
      "Please try again",
      Alert.AlertType.INFORMATION
    );
  }
}
