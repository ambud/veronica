package org.veronicadb.core.diskgraph.operations;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.veronicadb.core.VException;
import org.veronicadb.core.structures.VElement;

public abstract class AbstractGraphOperation {
	
	public abstract void doOperation(Consumer<AbstractGraphOperationResult> callback) throws VException;
	
	public abstract void undoOperation() throws VException;
	
}
