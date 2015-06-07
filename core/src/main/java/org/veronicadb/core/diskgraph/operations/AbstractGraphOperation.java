package org.veronicadb.core.diskgraph.operations;

import java.util.function.Consumer;

import org.veronicadb.core.VException;

public abstract class AbstractGraphOperation {
	
	public abstract void doOperation(Consumer<AbstractGraphOperationResult> callback) throws VException;
	
	public abstract void undoOperation() throws VException;
	
}
