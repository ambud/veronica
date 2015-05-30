package org.veronica.core.diskgraph.operations;

import java.util.Arrays;
import java.util.List;

import org.veronica.core.structures.VElement;

public abstract class AbstractElementOperation extends AbstractGraphOperation {

	private List<VElement> data;
	
	public AbstractElementOperation(List<VElement> dataElements) {
		this.data = dataElements;
	}
	
	public AbstractElementOperation(VElement dataElement) {
		data = Arrays.asList(dataElement);
	}
	
	public List<VElement> getData() {
		return data;
	}
	
}
