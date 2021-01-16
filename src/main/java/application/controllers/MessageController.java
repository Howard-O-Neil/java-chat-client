package application.controllers;

import application.App;
import application.Utils.HttpClientHelper;
import application.Utils.IConsumer2;
import application.models.Message;
import application.models.MessageFileType;
import application.models.Response;
import application.models.User;
import application.views.MessagePage;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javafx.application.Platform;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class MessageController {

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
      public void afterConnected(
        StompSession session,
        StompHeaders connectedHeaders
      ) {
        session.subscribe(
          "/room/" + App._userInstance.getUser().getUserName(),
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
                var response = new GenericParser<Response<Message>>(){}
                  .parse(payload);
      
                Message message = response.getData();
                if (response.getStatus() != 200) return;
                Platform.runLater(
                  new Runnable() {
                    @Override
                    public void run() {
                      int index = MessagePage
                        .getInstance()
                        .getRoom()
                        .getMessageIndex();
                      MessagePage.getInstance().getRoom().addLastMessage(message);
                      MessagePage
                        .getInstance()
                        .getRoom()
                        .setMessageIndex(index + 1);
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

  private void getMessage(String sender, String receiver, 
    IConsumer2<List<Message>> consumer) throws Exception {
    //
    HttpGet request = new HttpGet(App.apiUrl +
      "message/get?sender=" + sender +
      "&receiver=" + receiver +
      "&index=" + MessagePage.getInstance().getRoom().getMessageIndex()
    );
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

      var response = new GenericParser<Response<List<Message>>>(){}
        .parse(responseBody);

      System.out.println("----------------------------------------");
      System.out.println(response.getData().size());
      System.out.println(response.toString());

      consumer.run(response.getData());
    });
  }

  public void sendMessage(String receiver, String content, MessageFileType fileType, String fileContent) {
    Message message = new Message(
      App._userInstance.getUser().getUserName(),
      receiver,content,fileType.toString(), 
      fileContent
    );

    session.send("/service/chat", message);
  }

  public void loadFromConversation(String receiver) throws Exception {
    Platform.runLater(
      new Runnable() {
        @Override
        public void run() {
          MessagePage.getInstance().openChatRoom(receiver);
        }
      }
    );
  }

  public void loadMessasgeFromConversation(String receiver) throws Exception {
    User user = App._userInstance.getUser();

    getMessage(user.getUserName(), receiver, list -> {
      Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            int index = MessagePage.getInstance().getRoom().getMessageIndex();
            int count = list.size();
            for (int i = 0; i < count; i++) {
              MessagePage.getInstance().getRoom().addMessage(list.get(i));
            } 
            if (count >= 15) {
              MessagePage.getInstance().getRoom().setLoadMoreVisibility(true);
            } else {
              MessagePage.getInstance().getRoom().setLoadMoreVisibility(false);
            }
            MessagePage.getInstance().getRoom().setMessageIndex(index + count);
            MessagePage.getInstance().getRoom().setIsLoading(false);
          }
        }
      );
    });
  }

  public void loadMessage() throws Exception {
    if (session == null) {
      connectAndSubcribe();
    }
  }
}
