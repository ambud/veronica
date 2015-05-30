/*
 * Copyright 2015 Ambud Sharma
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.2
 */
package org.veronicadb.core.structures;


public class VEdge extends VElement {
	
	private VGraphShard outVGraph;
	private String inV;
	private String outV;
	
	public VEdge(String id, String label, String inV, VGraphShard inGraph, String outV, VGraphShard outGraph) {
		super(inGraph, id, label);
		this.inV = inV;
		this.outV = outV;
		this.outVGraph = outGraph;
	}
	
	/**
	 * @return the inV
	 */
	public VVertex getInVertex() {
		return getGraphShard().getVertex(inV);
	}

	/**
	 * @return the outV
	 */
	public VVertex getOutVertex() {
		return getGraphShard().getVertex(outV);
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
	public VGraphShard getOutVGraph() {
		return outVGraph;
	}

	/**
	 * If the supplied vertex is the inner vertex of this edge
	 * @param vertex
	 * @return is inner vertex
	 */
	public boolean isInV(String vertexId) {
		return inV.equalsIgnoreCase(vertexId);
	}
	
	/**
	 * Get other vertex of this edge
	 * @param vertex
	 * @return other vertex
	 */
	public VVertex getOtherVertex(VVertex vertex) {
		return inV.equalsIgnoreCase(vertex.getId())?getOutVertex():getInVertex();
	}
	
	public String getGraphId(String vertexId) {
		if(isInV(vertexId)) {
			return getGraphShard().getShardId();
		}else{
			return outVGraph.getShardId();
		}
	}
	
	/**
	 * @param vertexId
	 * @return other vertex
	 */
	public String getOtherVertex(String vertexId) {
		return inV.equalsIgnoreCase(vertexId)?outV:inV;
	}
	
	
}
