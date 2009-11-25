package bazooka.common.service;

import bazooka.common.exception.ExistingConfigurationException;
import bazooka.common.exception.NonExistingConfigurationException;
import bazooka.common.model.Configuration;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GWT.rpc")
public interface ConfigurationService extends RemoteService {

  Configuration saveConfiguration(Configuration configuration) throws ExistingConfigurationException;

  Configuration updateConfiguration(Configuration configuration) throws NonExistingConfigurationException;

  void deleteConfiguration(Configuration configuration) throws NonExistingConfigurationException;

  Configuration getConfiguration(String name) throws NonExistingConfigurationException;

  List<Configuration> listConfigurations();
}