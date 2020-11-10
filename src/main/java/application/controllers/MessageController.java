package application.controllers;

import application.App;
import application.models.Conversation;
import application.models.Message;
import application.models.Response;
import application.models.User;
import application.views.MessagePage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MessageController {

    StompSession session;

    void connectAndSubcribe() throws Exception{
        WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/room/" + App._userInstance.getUser().getUserName(), new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders stompHeaders) {
                        return Response.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders stompHeaders, Object payload) {
                        System.out.println("-----------------------------------------");
                        System.out.println("Receive from server:");
                        try{
                            Gson gson = new Gson();
                            String json = gson.toJson(payload);
                            System.out.println(json);
                            Response<Message> response = gson.fromJson(json, new TypeToken<Response<Message>>(){}.getType());
                            Message message = response.getData();
                            if(response.getStatus() != 200) return;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    int index = MessagePage.getInstance().getRoom().getMessageIndex();
                                    MessagePage.getInstance().getRoom().addMessage(message);
                                    MessagePage.getInstance().getRoom().setMessageIndex(index + 1);
                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        session = stompClient.connect(App.messageSocketUrl, sessionHandler).get();
    }

    private List<Message> getMessage(String sender, String receiver) throws Exception{
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        try {
            client.start();
            HttpGet request = new HttpGet(App.apiUrl +"message/get?sender=" + sender + "&receiver=" + receiver + "&index=" + MessagePage.getInstance().getRoom().getMessageIndex());

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
            Gson gson = new Gson();
            Response<List<Message>> response = gson.fromJson(responseBody, new TypeToken<Response<List<Message>>>(){}.getType());

            return response.getData();

        }finally {
            client.close();
        }
    }

    public void sendMessage(String receiver, String content){
        Message message = new Message(
                App._userInstance.getUser().getUserName(),
                receiver,
                content
        );

        session.send("/service/chat", message);
    }

    public void loadFromConversation(String receiver) throws Exception{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MessagePage.getInstance().openChatRoom(receiver);
            }
        });
    }

    public void loadMessasgeFromConversation(String receiver) throws Exception{
        User user = App._userInstance.getUser();

        List<Message> list = getMessage(user.getUserName(), receiver);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int index = MessagePage.getInstance().getRoom().getMessageIndex();
                int count = list.size();
                for(int i = count - 1; i >=0; i-- ){
                    MessagePage.getInstance().getRoom().addMessage(list.get(i));
                }
                MessagePage.getInstance().getRoom().setMessageIndex(index + count);
            }
        });
    }

    public void loadMessage() throws Exception{
        if(session == null) {
            connectAndSubcribe();
        }
    }
}
