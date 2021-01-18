package application.Utils;

import java.io.File;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

import application.App;

public class FormHelper {
  public static void submitData(String fileUrl, IConsumer2<HttpResponse> consumer) throws Exception {
    File file = new File(fileUrl);
    HttpEntity submitEntity = MultipartEntityBuilder
      .create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
      .addBinaryBody("files", file)
      .build();

    HttpPost httpPost = new HttpPost(App.cdnUrl + "upload");
    httpPost.setEntity(submitEntity);

    HttpClientHelper.start(httpPost, httpResponse -> {
      consumer.run(httpResponse);
    });
  } 

  public static void submitData(File file, IConsumer2<HttpResponse> consumer) throws Exception {
    HttpEntity submitEntity = MultipartEntityBuilder
      .create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
      .addBinaryBody("files", file)
      .build();

    HttpPost httpPost = new HttpPost(App.cdnUrl + "upload");
    httpPost.setEntity(submitEntity);

    HttpClientHelper.start(httpPost, httpResponse -> {
      consumer.run(httpResponse);
    });
  }

  public static void test() throws Exception {    
    File file = new File("/home/larryjason/project/java-chat-client/src/main/resources/images/user-avatar.png");
    HttpEntity submitEntity = MultipartEntityBuilder
      .create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
      .addBinaryBody("files", file)
      .build();

    HttpPost httpPost = new HttpPost(App.cdnUrl + "upload");
    httpPost.setEntity(submitEntity);

    HttpClientHelper.start(httpPost, httpResponse -> {
      HttpEntity entity = httpResponse.getEntity();
      String responseBody = entity != null ? EntityUtils.toString(entity) : null;

      System.out.println(responseBody);
    });
    
  }
}
