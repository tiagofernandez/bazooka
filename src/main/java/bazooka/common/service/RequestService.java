package bazooka.common.service;

import bazooka.common.exception.ExistingRequestException;
import bazooka.common.exception.NonExistingRequestException;
import bazooka.common.model.Request;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GWT.rpc")
public interface RequestService extends RemoteService {

  Request saveRequest(Request request) throws ExistingRequestException;

  Request updateRequest(Request request) throws NonExistingRequestException;

  void deleteRequest(Request request) throws NonExistingRequestException;

  Request getRequest(String name) throws NonExistingRequestException;

  List<Request> listRequests();
}