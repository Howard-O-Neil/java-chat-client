package application.Utils;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import application.App;

public class HttpClientHelper {
  public static void start(HttpPost request, IConsumer2<HttpResponse> callBack) throws Exception {
    //
    App.executor.execute(() -> {
      CloseableHttpClient client = HttpClients.createDefault();
      try {
        HttpResponse response = client.execute(request);
        callBack.run(response);
  
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {

        try {
          client.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static void start(HttpGet request, 
    IConsumer2<HttpResponse> callBack) throws Exception {
    //
    App.executor.execute(() -> {
      CloseableHttpClient client = HttpClients.createDefault();
      try {
        HttpResponse response = client.execute(request);
        callBack.run(response);
  
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {

        try {
          client.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
