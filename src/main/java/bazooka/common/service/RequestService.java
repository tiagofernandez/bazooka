package bazooka.common.service;

import bazooka.common.exception.ExistingRequestException;
import bazooka.common.exception.NonExistingRequestException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GWT.rpc")
public interface RequestService extends RemoteService {

  void saveRequest(String request, String payload) throws ExistingRequestException;

  void updateRequest(String request, String payload) throws NonExistingRequestException;

  String getPayload(String request) throws NonExistingRequestException;

  void deleteRequest(String request) throws NonExistingRequestException;

  List<String> listRequests();
}