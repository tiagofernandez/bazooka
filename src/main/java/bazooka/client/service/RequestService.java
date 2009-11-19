package bazooka.client.service;

import bazooka.client.exception.ExistingRequestException;
import bazooka.client.exception.NonExistingRequestException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GWT.rpc")
public interface RequestService extends RemoteService {

  void saveRequest(String name, String payload) throws ExistingRequestException;

  void deleteRequest(String name) throws NonExistingRequestException;

  List<String> listRequests();

  void savePayload(String payload, String request);

  String getPayload(String request);
}