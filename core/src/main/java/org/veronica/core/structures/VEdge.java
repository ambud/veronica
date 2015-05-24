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
	public VVertex getInV() {
		return inV;
	}

	/**
	 * @return the outV
	 */
	public VVertex getOutV() {
		return outV;
	}
	
	/**
	 * Get other vertex of this edge
	 * @param vertex
	 * @return other vertex
	 */
	public VVertex getOtherVertex(VVertex vertex) {
		return inV.getId().equalsIgnoreCase(vertex.getId())?outV:inV;
	}
	
}
