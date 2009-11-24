package bazooka.common.service;

import bazooka.common.model.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface RequestServiceAsync {

  void saveRequest(Request request, AsyncCallback<Request> async);

  void updateRequest(Request request, AsyncCallback<Request> async);

  void deleteRequest(Request request, AsyncCallback<Void> async);

  void getRequest(String name, AsyncCallback<Request> async);

  void listRequests(AsyncCallback<List<Request>> async);
}
