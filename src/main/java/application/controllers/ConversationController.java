package application.controllers;

import application.Main;
import application.models.Conversation;
import application.models.Response;
import application.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Future;

public class ConversationController {

    int conversationIndex = 0;

    WebSocketClient webSocketClient = null;

    public void connectAndSubcribe(){
        webSocketClient = new WebSocketClient(URI.create(Main.messageSocketUrl + "socket-service")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }


            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onClose(int i, String s, boolean b) {
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        };
        webSocketClient.connect();
    }

    public void close(){
        if(webSocketClient!=null)
            webSocketClient.close();
    }

    public Boolean addNewConversation(Conversation conversation) throws Exception{
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        try {
            client.start();

            HttpPost request = new HttpPost(Main.apiUrl + "conversation/check");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(conversation);
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
            if(responseBody == null){
                return false;
            }
            else if (responseBody.contains("conversation not available")){
                AlertDialog.makeAler(
                        "Conversation",
                        "conversation not available",
                        "Please find another conversation",
                        Alert.AlertType.INFORMATION
                );
                return false;
            }
            else{
                notifyNewConversation(conversation);
                return true;
            }
        }
        finally {
            client.close();
        }
    }

    void notifyNewConversation(Conversation conversation) {

    }

    public Conversation getConversation(String username) throws Exception{
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        try {
            client.start();
            HttpGet request = new HttpGet(Main.apiUrl +"conversation/get?username=" + username +"&index=" + conversationIndex);

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

            Gson gson = new Gson();
            Response<Conversation> response = gson.fromJson(responseBody, new TypeToken<Response<Conversation>>(){}.getType());

            return response.getData();
        }
        finally {
            client.close();
        }
    }

    public void loadConversation(){
        User user = Main._user;
        if(webSocketClient == null){
            connectAndSubcribe();
        }
//        try {
//            var res = getConversation(user.getUserName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
