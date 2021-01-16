package application.controllers;

import application.App;
import application.Utils.HttpClientHelper;
import application.Utils.IConsumer2;
import application.models.Conversation;
import application.models.Message;
import application.models.Response;
import application.models.User;
import application.views.MessagePage;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class ConversationController {

  public StompSession session;

  void connectAndSubcribe() throws Exception {
    WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(simpleWebSocketClient));

    SockJsClient sockJsClient = new SockJsClient(transports);
    WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
      @Override
      public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe(
          "/conversation/" + App._userInstance.getUser().getUserName(),
          new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
              return Response.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object payload) {
              System.out.println("-----------------------------------------");
              System.out.println("Receive from server:");
              try {
                var response = new GenericParser<Response<Conversation>>() {}.parse(payload);

                Conversation conversation = response.getData();
                if (response.getStatus() != 200) return;
                Platform.runLater(
                  new Runnable() {
                    @Override
                    public void run() {
                      int index = MessagePage.getInstance().getConversationIndex();
                      MessagePage.getInstance().addConversation(conversation);
                      MessagePage.getInstance().setConversationIndex(index + 1);
                    }
                  }
                );
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
        );
      }
    };
    session = stompClient.connect(App.messageSocketUrl, sessionHandler).get();
  }

  private void notifyNewConversation(String username) {
    Conversation conversation = new Conversation(
      App._userInstance.getUser().getUserName(),
      username
    );

    session.send("/service/notify-conversation", conversation);
  }

  private void getConversation(String username, IConsumer2<List<Conversation>> consumer)
    throws Exception {
    HttpGet request = new HttpGet(
      App.apiUrl +
      "conversation/get?username=" +
      username +
      "&index=" +
      MessagePage.getInstance().getConversationIndex()
    );

    HttpClientHelper.start(
      request,
      httpResponse -> {
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

        var response = new GenericParser<Response<List<Conversation>>>() {}.parse(responseBody);

        consumer.run(response.getData());
      }
    );
  }

  public void addNewConversation(String username) throws Exception {
    Conversation conversation = new Conversation(
      App._userInstance.getUser().getUserName(),
      username
    );
    HttpPost request = new HttpPost(App.apiUrl + "conversation/check");
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-type", "application/json");

    Gson gson = new Gson();
    String json = gson.toJson(conversation);
    request.setEntity(new StringEntity(json));

    HttpClientHelper.start(
      request,
      httpResponse -> {
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
        if (responseBody == null || responseBody.contains("conversation not available")) {
          Platform.runLater(
            () -> {
              noConversation();
            });
        } else {
          notifyNewConversation(username);
        }
      }
    );
  }

  private void noConversation() {
    AlertDialog.makeAler(
      "Conversation",
      "conversation not available",
      "Please find another conversation",
      Alert.AlertType.INFORMATION
    );
  }

  public void loadConversation() throws Exception {
    User user = App._userInstance.getUser();
    if (session == null) {
      connectAndSubcribe();
    }

    getConversation(
      user.getUserName(),
      list -> {
        Platform.runLater(
          () -> {
            int index = MessagePage.getInstance().getConversationIndex();
            int count = list.size();
            for (int i = count - 1; i >= 0; i--) {
              MessagePage.getInstance().addConversation(list.get(i));
            }
            MessagePage.getInstance().setConversationIndex(index + count);
          });
      }
    );
  }
}
