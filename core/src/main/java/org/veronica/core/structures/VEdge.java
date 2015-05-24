package org.veronica.core.structures;

public class VEdge extends VElement {
	
	private VSubGraph outVGraph;
	private String inV;
	private String outV;
	
	protected VEdge(String id, String label, VVertex inV, VVertex outV) {
		super(inV.getGraph(), id, label);
		this.inV = inV.getId();
		this.outV = outV.getId();
		this.outVGraph = outV.getGraph();
	}
	
	/**
	 * @return the inV
	 */
	public VVertex getInVertex() {
		return getGraph().getVertex(inV);
	}

	/**
	 * @return the outV
	 */
	public VVertex getOutVertex() {
		return getGraph().getVertex(outV);
	}
	
	/**
	 * @return the inV
	 */
	protected String getInV() {
		return inV;
	}

	/**
	 * @return the outV
	 */
	protected String getOutV() {
		return outV;
	}

	/**
	 * @return the outVGraph
	 */
	protected VSubGraph getOutVGraph() {
		return outVGraph;
	}

	/**
	 * Get other vertex of this edge
	 * @param vertex
	 * @return other vertex
	 */
	public VVertex getOtherVertex(VVertex vertex) {
		return inV.equalsIgnoreCase(vertex.getId())?getOutVertex():getInVertex();
	}
	
}
