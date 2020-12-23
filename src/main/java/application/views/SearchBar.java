package application.views;

import application.App;
import application.models.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

public class SearchBar extends TextField {

  class GetEntriesThread extends Thread {

    public GetEntriesThread() {}

    @Override
    public void run() {
      try {
        getEntriesFromApi();
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }

    Runnable updateEntries = new Runnable() {
      @Override
      public void run() {
        entriesPopupUpdate();
      }
    };

    void getEntriesFromApi() throws Exception {
      CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
      try {
        client.start();
        HttpGet request = new HttpGet(
          App.apiUrl + "user/find?searchkey=" + getText()
        );

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
        Response<List<String>> response = gson.fromJson(
          responseBody,
          new TypeToken<Response<List<String>>>() {}.getType()
        );
        entries.clear();
        entries.addAll(response.getData());

        Platform.runLater(updateEntries);
      } finally {
        client.close();
      }
    }
  }

  private SortedSet<String> entries;
  private ContextMenu entriesPopup;

  private final int _max_entries = 10;

  public SearchBar() {
    super();
    entries = new TreeSet<>();
    entriesPopup = new ContextMenu();

    textProperty()
      .addListener(
        new ChangeListener<String>() {
          @Override
          public void changed(
            ObservableValue<? extends String> observableValue,
            String s,
            String s2
          ) {
            if (getText().length() < 3) {
              entriesPopup.hide();
            } else {
              GetEntriesThread thread = new GetEntriesThread();
              thread.start();
            }
          }
        }
      );

    focusedProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observableValue,
            Boolean aBoolean,
            Boolean aBoolean2
          ) {
            entriesPopup.hide();
          }
        }
      );
  }

  void entriesPopupUpdate() {
    LinkedList<String> searchResult = new LinkedList<>();
    searchResult.addAll(entries);
    if (entries.size() > 0) {
      populatePopup(searchResult);
      if (!entriesPopup.isShowing()) {
        entriesPopup.show(SearchBar.this, Side.BOTTOM, 0, 0);
      }
    } else {
      entriesPopup.hide();
    }
  }

  void populatePopup(List<String> searchResult) {
    List<CustomMenuItem> menuItems = new LinkedList<>();
    int maxEntries = _max_entries;
    int count = Math.min(searchResult.size(), maxEntries);
    for (int i = 0; i < count; i++) {
      final String result = searchResult.get(i);
      Label entryLabel = new Label(result);
      CustomMenuItem item = new CustomMenuItem(entryLabel, true);
      item.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
            setText(result);
            entriesPopup.hide();
          }
        }
      );
      menuItems.add(item);
    }
    entriesPopup.getItems().clear();
    entriesPopup.getItems().addAll(menuItems);
  }
}
