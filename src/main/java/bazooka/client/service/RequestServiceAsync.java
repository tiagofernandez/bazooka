package bazooka.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface RequestServiceAsync {

  void saveRequest(String name, String payload, AsyncCallback<Void> async);

  void deleteRequest(String name, AsyncCallback<Void> async);

  void listRequests(AsyncCallback<List<String>> async);

  void savePayload(String payload, String request, AsyncCallback<Void> async);

  void getPayload(String request, AsyncCallback<String> async);
}
