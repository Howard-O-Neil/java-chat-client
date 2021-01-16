package application.controllers;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import application.App;
import application.Utils.HttpClientHelper;
import application.Utils.IConsumer2;
import application.models.Response;
import application.models.User;
import application.views.ConversationCell;
import javafx.application.Platform;

public class UserController {
  User user;

  public User getUser() {
    return user;
  }

  private void requestUser(String username, IConsumer2<User> consumer) throws Exception {
    HttpGet request = new HttpGet(App.apiUrl + "user/get?username=" + username);
    HttpClientHelper.start(request, httpResponse -> {
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

      var response = new GenericParser<Response<User>>(){}.parse(responseBody);
      consumer.run(response.getData());
    });
  }

  public void requestConversationSignature(String username, ConversationCell conversationCell) throws Exception {
    requestUser(username, user -> {
      Platform.runLater(() -> {
        conversationCell.setSignature("Id: " + user.getId().toString().substring(0, 10));
      });
    });
  }

  public void setUser(User user) {
    this.user = user;
  }
}
