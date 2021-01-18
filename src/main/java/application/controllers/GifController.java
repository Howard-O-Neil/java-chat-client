package application.controllers;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import application.App;
import application.Utils.HttpClientHelper;
import application.Utils.IConsumer2;
import application.models.Giphy;
import application.views.GifPicker;
import application.views.MessagePage;
import application.models.Response;

public class GifController {
  final int limitGifEachRequest = 2;
  private void getGif(IConsumer2<List<Giphy.Root>> consumer) throws Exception {
    HttpGet request = null;
    if (MessagePage.getInstance().getRoom().getGifPicker().getMode() == "trending") {
      request = new HttpGet("http://api.giphy.com/v1/gifs/trending" + "?api_key=" + App.giphyApiKey + "&limit=2"
        + "&offset=" + MessagePage.getInstance().getRoom().getGifPicker().getCurrentOffset());
    } else {
      request = new HttpGet("http://api.giphy.com/v1/gifs/search" + "?api_key=" + App.giphyApiKey 
        + "&q=" + MessagePage.getInstance().getRoom().getGifPicker().getSearchString() 
        + "&limit=2"
        + "&offset=" + MessagePage.getInstance().getRoom().getGifPicker().getCurrentOffset());
    }
     
    System.out.println(request.getRequestLine().getUri());
    HttpClientHelper.start(request, httpResponse -> {
      String responseBody;
      int status = httpResponse.getStatusLine().getStatusCode();
      if (status >= 200 && status < 300) {
        HttpEntity entity = httpResponse.getEntity();
        responseBody = entity != null ? EntityUtils.toString(entity) : null;
      } else {
        throw new ClientProtocolException("Unexpected response status: " + status);
      }

      var response = new GenericParser<Response<List<Giphy.Root>>>() {
        }.parse(responseBody);


      consumer.run(response.getData());
    });
  }

  public void loadGif() {
    try {
      this.getGif(list -> {
        // System.out.println(response.get(0).toString());
        GifPicker picker = MessagePage.getInstance().getRoom().getGifPicker();
        App.executor.execute(() -> {
          System.out.println("----------------------------------------");
          for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).id);

            picker.addGif(list.get(i));
          }
          picker.addCurrentOffset(limitGifEachRequest + 1);
        });
        
      });
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
