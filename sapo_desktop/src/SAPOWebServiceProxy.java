/**
 * @author Edison Puig Maldonado
 *
 */

public class SAPOWebServiceProxy implements SAPOWebService {
  private String _endpoint = null;
  private SAPOWebService sAPOWebService = null;
  
  public SAPOWebServiceProxy() {
    _initSAPOWebServiceProxy();
  }
  
  private void _initSAPOWebServiceProxy() {
    try {
      sAPOWebService = (new SAPOWebServiceServiceLocator()).getSAPOWebService();
      if (sAPOWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sAPOWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sAPOWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sAPOWebService != null)
      ((javax.xml.rpc.Stub)sAPOWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SAPOWebService getSAPOWebService() {
    if (sAPOWebService == null)
      _initSAPOWebServiceProxy();
    return sAPOWebService;
  }
  
  public int recebeDados(java.lang.String username, java.lang.String dadosXML, java.lang.String senha) throws java.rmi.RemoteException{
    if (sAPOWebService == null)
      _initSAPOWebServiceProxy();
    return sAPOWebService.recebeDados(username, dadosXML, senha);
  }
  
  public java.lang.String consulta(java.lang.String optionsXML) throws java.rmi.RemoteException{
    if (sAPOWebService == null)
      _initSAPOWebServiceProxy();
    return sAPOWebService.consulta(optionsXML);
  }
  
  
}