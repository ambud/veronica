package org.veronica.core.server.protocol;

import java.util.ArrayList;
import java.util.List;

import org.veronica.core.structures.VElement;

public abstract class VGraphOperation {

	protected byte operationCode;
	private List<VElement> element;
	
	public VGraphOperation() {
		element = new ArrayList<VElement>();
	}

	public VGraphOperation(byte operationCode, List<VElement> element) {
		this.operationCode = operationCode;
		this.element = element;
	}

	public List<VElement> getElement() {
		return element;
	}

	public byte getOperationCode() {
		return operationCode;
	}
	
	public int getOperationCount() {
		return element!=null?element.size():0;
	}
	
}
