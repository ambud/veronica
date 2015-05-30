package org.veronica.core.diskgraph.operations;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.veronica.core.VException;
import org.veronica.core.structures.VElement;

public abstract class AbstractGraphOperation {
	
	public abstract void doOperation(Consumer<AbstractGraphOperationResult> callback) throws VException;
	
	public abstract void undoOperation() throws VException;
	
}
