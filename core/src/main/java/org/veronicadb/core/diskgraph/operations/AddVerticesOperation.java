package org.veronicadb.core.diskgraph.operations;

import java.util.List;
import java.util.function.Consumer;

import org.veronicadb.core.VException;
import org.veronicadb.core.structures.VElement;

public class AddVerticesOperation extends AbstractElementOperation {

	public AddVerticesOperation(List<VElement> dataElements) {
		super(dataElements);
	}
	
	public AddVerticesOperation(VElement dataElement) {
		super(dataElement);
	}

	@Override
	public void doOperation(Consumer<AbstractGraphOperationResult> callback)
			throws VException {
		
	}

	@Override
	public void undoOperation() throws VException {
		// TODO Auto-generated method stub

	}

}
