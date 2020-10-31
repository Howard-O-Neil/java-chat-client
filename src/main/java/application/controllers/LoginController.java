package application.controllers;

import application.Main;
import application.models.Response;
import application.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

public class LoginController implements Initializable {

    @FXML
    Button login_btn;
    @FXML
    ImageView logo_img;

    @FXML
    TextField username;
    @FXML
    PasswordField password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img = new Image("/images/chat-icon.png");
        logo_img.setImage(img);
    }

    @FXML
    public void login(ActionEvent actionEvent) throws Exception{

        User user = new User(username.getText(),password.getText());

        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        try {
            client.start();

            HttpPost request = new HttpPost(Main.apiUrl + "user/checklogin");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(user);
            StringEntity stringEntity = new StringEntity(json);
            request.setEntity(stringEntity);

            Future<HttpResponse> future = client.execute(request, null);
            HttpResponse httpResponse = future.get();
            HttpEntity entity = httpResponse.getEntity();
            String responseBody;
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                entity = httpResponse.getEntity();
                responseBody = entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("----------------------------------------");
            System.out.println(responseBody);

            if (responseBody.contains("login-failed")) {
                AlertDialog.makeAler(
                    "Login failed",
                    "Wrong username or password",
                    "Please try enter a correct username and password",
                    Alert.AlertType.INFORMATION
                );
                return;
            }

            Response<User> response = gson.fromJson(responseBody, new TypeToken<Response<User>>() {}.getType());
            Main._user = response.getData();

            openChatWindow();

        } finally {
            client.close();
        }
    }

    void openChatWindow() throws IOException{
        ((Stage)login_btn.getScene().getWindow()).close();

        Stage main = new Stage();

        Parent main_root = FXMLLoader.load(getClass().getResource("/views/main_scene.fxml"));
        Scene main_scene = new Scene(main_root, 960, 640);
        main_scene.getStylesheets().add(Main.class.getResource("/styles/main_style.css").toExternalForm());
        main.setScene(main_scene);
        main.setTitle("Chat app");
        main.setResizable(false);
        main.show();
    }
}
