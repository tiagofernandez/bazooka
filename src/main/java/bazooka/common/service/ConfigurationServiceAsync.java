package bazooka.common.service;

import bazooka.common.model.Configuration;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ConfigurationServiceAsync {

  void saveConfiguration(Configuration configuration, AsyncCallback<Configuration> async);

  void updateConfiguration(Configuration configuration, AsyncCallback<Configuration> async);

  void deleteConfiguration(Configuration configuration, AsyncCallback<Void> async);

  void getConfiguration(String name, AsyncCallback<Configuration> async);

  void listConfigurations(AsyncCallback<List<Configuration>> async);
}