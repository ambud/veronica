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
package org.veronica.core.structures;

import java.util.ArrayList;
import java.util.List;

public class VVertex extends VElement {
	
	private List<VEdge> edges;

	public VVertex(String id, String label) {
		super(id, label);
		edges = new ArrayList<VEdge>();
	}
	
	/**
	 * Add an edge between this vertex and the supplied vertex. 
	 * 
	 * This will establish a bi-di relationship between the two vertex objects
	 * 
	 * @param vertex
	 * @param isInV is the supplied vertex in vertex of this edge
	 */
	public void addEdge(VVertex vertex, String label, boolean isInV) {
		if(isInV) {
			VEdge edge = new VEdge(null, label, vertex, this);
			this.edges.add(edge);
			vertex.edges.add(edge);
		}
	}
	
}
