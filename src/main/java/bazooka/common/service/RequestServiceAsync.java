package bazooka.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface RequestServiceAsync {

  void saveRequest(String request, String payload, AsyncCallback<Void> async);

  void deleteRequest(String request, AsyncCallback<Void> async);

  void listRequests(AsyncCallback<List<String>> async);

  void getPayload(String request, AsyncCallback<String> async);

  void updateRequest(String request, String payload, AsyncCallback<Void> async);
}
