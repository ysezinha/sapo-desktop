

public interface SAPOWebServiceService extends javax.xml.rpc.Service {
    public java.lang.String getSAPOWebServiceAddress();

    public SAPOWebService getSAPOWebService() throws javax.xml.rpc.ServiceException;

    public SAPOWebService getSAPOWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
