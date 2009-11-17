package bazooka.server.service

import com.google.inject._
import com.google.gwt.user.client.rpc._
import com.google.gwt.user.server.rpc._

@Singleton
class BazookaRemoteServiceServlet extends RemoteServiceServlet {

  @Inject private var injector: Injector = _

  override def processCall(payload: String) = {
    try {
      val request = RPC.decodeRequest(payload, null, this)
      val service = getServiceInstance(request.getMethod.getDeclaringClass);

      RPC.invokeAndEncodeResponse(service, request.getMethod, request.getParameters, request.getSerializationPolicy)
    }
    catch {
      case ex: IncompatibleRemoteServiceException =>
        log("IncompatibleRemoteServiceException in the processCall(String) method", ex)
        RPC.encodeResponseForFailure(null, ex);
    }
  }

  private def getServiceInstance(serviceClass: Class[_]) = injector.getInstance(serviceClass)
}