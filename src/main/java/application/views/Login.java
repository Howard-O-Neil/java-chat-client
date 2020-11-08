package application.views;

import application.App;
import application.controllers.AlertDialog;
import application.models.Response;
import application.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import javafx.scene.layout.VBox;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.Future;

public class Login extends VBox {

    class LoginAuthThread extends Thread{
        @Override
        public void run() {
            try{
                login();
            }catch (Exception e){
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
                } catch (Exception e){
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
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
                System.out.println("----------------------------------------");
                System.out.println(responseBody);

                if (responseBody == null || responseBody.contains("login-failed")) {
                    Platform.runLater(failed);
                }
                else{
                    Response<User> response = gson.fromJson(responseBody, new TypeToken<Response<User>>() {
                    }.getType());
                    App._userInstance.setUser(response.getData());

                    Platform.runLater(success);
                }

            } finally {
                client.close();
            }

            progessing = false;
        }
    }

    class SignUpThread extends Thread{
        @Override
        public void run() {
            try{
                signup();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Runnable failed = new Runnable() {
            @Override
            public void run() {
                AlertDialog.makeAler(
                        "Sign up failed",
                        "Terrible username or password",
                        "Please try enter a better username and password",
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
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        void signup() throws Exception {
            User user = new User(username.getText(), password.getText());

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
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
                System.out.println("----------------------------------------");
                System.out.println(responseBody);

                if (responseBody == null || responseBody.contains("login-failed")) {
                    Platform.runLater(failed);
                }
                else{
                    Response<Boolean> response = gson.fromJson(responseBody, new TypeToken<Response<Boolean>>() {
                    }.getType());
                    if(response.getData() == true){
                        Platform.runLater(success);
                    }
                    else{
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
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Button login_button;
    @FXML
    Hyperlink signup_link;
    @FXML
    ImageView logo_img;

    boolean progessing = false;

    public Login(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        this.getStylesheets().add(getClass().getResource("/styles/login_style.css").toExternalForm());
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        BooleanProperty firstTime = new SimpleBooleanProperty(true);
        username.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                this.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    if(!progessing){
                        progessing = true;
                        LoginAuthThread thread = new LoginAuthThread();
                        thread.start();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        signup_link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    if(!progessing){
                        progessing = true;
                        SignUpThread thread = new SignUpThread();
                        thread.start();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        loadResource();
    }

    private void loadResource(){
        Image img = new Image("/images/chat-logo.png");
        logo_img.setImage(img);
    }

    private void openMainScene() throws IOException {
        HBox parent = (HBox)getParent();
        parent.getChildren().clear();
        parent.getChildren().add(new Main());
    }

    private void loginFailed(){

    }
    private void signFailed(){
        AlertDialog.makeAler(
                "Login failed",
                "Wrong username or password",
                "Please try enter a correct username and password",
                Alert.AlertType.INFORMATION
        );
    }
}
