

public class SAPOWebServiceServiceLocator extends org.apache.axis.client.Service implements SAPOWebServiceService {
	
	public SAPOWebServiceServiceLocator() {
	}
	
	
	public SAPOWebServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}
	
	public SAPOWebServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}
	
	// Use to get a proxy class for SAPOWebService
	private java.lang.String SAPOWebService_address = SAPO.BASEWEB + "/services/SAPOWebService";
	
	public java.lang.String getSAPOWebServiceAddress() {
		return SAPOWebService_address;
	}
	
	// The WSDD service name defaults to the port name.
	private java.lang.String SAPOWebServiceWSDDServiceName = "SAPOWebService";
	
	public java.lang.String getSAPOWebServiceWSDDServiceName() {
		return SAPOWebServiceWSDDServiceName;
	}
	
	public void setSAPOWebServiceWSDDServiceName(java.lang.String name) {
		SAPOWebServiceWSDDServiceName = name;
	}
	
	public SAPOWebService getSAPOWebService() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(SAPOWebService_address);
		}
		catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getSAPOWebService(endpoint);
	}
	
	public SAPOWebService getSAPOWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			SAPOWebServiceSoapBindingStub _stub = new SAPOWebServiceSoapBindingStub(portAddress, this);
			_stub.setPortName(getSAPOWebServiceWSDDServiceName());
			return _stub;
		}
		catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}
	
	public void setSAPOWebServiceEndpointAddress(java.lang.String address) {
		SAPOWebService_address = address;
	}
	
	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (SAPOWebService.class.isAssignableFrom(serviceEndpointInterface)) {
				SAPOWebServiceSoapBindingStub _stub = new SAPOWebServiceSoapBindingStub(new java.net.URL(SAPOWebService_address), this);
				_stub.setPortName(getSAPOWebServiceWSDDServiceName());
				return _stub;
			}
		}
		catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}
	
	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("SAPOWebService".equals(inputPortName)) {
			return getSAPOWebService();
		}
		else  {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}
	
	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://DefaultNamespace", "SAPOWebServiceService");
	}
	
	private java.util.HashSet ports = null;
	
	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://DefaultNamespace", "SAPOWebService"));
		}
		return ports.iterator();
	}
	
	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
		
		if ("SAPOWebService".equals(portName)) {
			setSAPOWebServiceEndpointAddress(address);
		}
		else 
		{ // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}
	
	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}
	
}
