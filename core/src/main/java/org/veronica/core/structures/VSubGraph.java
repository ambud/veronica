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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VSubGraph {

	private String graphId;
	private Map<String, VVertex> shardVertices;
	// bloom filter for the vertices in this subgraph
	
	public VSubGraph() {
		graphId = UUID.randomUUID().toString();
		shardVertices = new ConcurrentHashMap<String, VVertex>();
	}
	
	public void addVertex(VVertex vertex) {
		shardVertices.put(vertex.getId(), vertex);
	}

	/**
	 * @return the graphId
	 */
	protected String getGraphId() {
		return graphId;
	}

	/**
	 * @return the shardVertices
	 */
	protected List<VVertex> getShardVertices() {
		return new ArrayList<VVertex>(shardVertices.values());
	}
	
}
