/**
 * @author Edison Puig Maldonado
 *
 */

public interface SAPOWebService extends java.rmi.Remote {
    public int recebeDados(java.lang.String username, java.lang.String dadosXML, java.lang.String senha) throws java.rmi.RemoteException;
    public java.lang.String consulta(java.lang.String optionsXML) throws java.rmi.RemoteException;
}
