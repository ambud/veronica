package org.veronica.core.structures;

public class VEdge extends VElement {
	
	private VVertex inV;
	private VVertex outV;
	
	protected VEdge(String id, String label, VVertex inV, VVertex outV) {
		super(id, label);
		this.inV = inV;
		this.outV = outV;
	}

	/**
	 * @return the inV
	 */
	protected VVertex getInV() {
		return inV;
	}

	/**
	 * @return the outV
	 */
	protected VVertex getOutV() {
		return outV;
	}
	
}
