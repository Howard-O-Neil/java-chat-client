package application.controllers;

import java.util.List;
import java.util.concurrent.Future;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import application.App;
import application.models.Response;
import application.models.User;
import application.views.ConversationCell;
import javafx.application.Platform;

public class UserController {
  User user;

  public User getUser() {
    return user;
  }

  private User requestUser(String username) throws Exception {
    CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
    try {
      client.start();
      HttpGet request = new HttpGet(App.apiUrl + "user/get?username=" + username);

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
      
      Gson gson = new Gson();
      Response<User> response = gson.fromJson(
        responseBody,
        new TypeToken<Response<User>>() {}.getType()
      );

      return response.getData();
    } finally {
      client.close();
    }
  }

  public void requestConversationSignature(String username, ConversationCell conversationCell) throws Exception {
    User user = requestUser(username);

    Platform.runLater(() -> {
      conversationCell.setSignature("Id: " + user.getId().toString().substring(0, 10));
    });
  }

  public void setUser(User user) {
    this.user = user;
  }
}
