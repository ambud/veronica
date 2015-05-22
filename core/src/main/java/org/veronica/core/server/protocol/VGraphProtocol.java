package org.veronica.core.server.protocol;

import java.io.Serializable;

public class VGraphProtocol implements Serializable {

	private static final long serialVersionUID = 3604892722500036094L;
	private byte protoVersion;
	private short operationCount;
	private VGraphOperation operations;
	
	public VGraphProtocol() {
	}
	
}
