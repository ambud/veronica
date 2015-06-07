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
package org.veronicadb.core.memorygraph.storage.strategies;

import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.veronicadb.core.memorygraph.VSubGraph;

/**
 * This Strategy family to decide how sharding of graph should be handled by Veronica.
 * 
 * A combination of flush and shard strategies should be used to get optimal performance.
 * 
 * @author ambudsharma
 *
 */
public abstract class VShardStrategy extends VStorageStrategy {
	
	public VShardStrategy(Configuration strategyConfig, String strategyName) {
		super(strategyConfig, strategyName);
	}

	/**
	 * The size of each shard created using this strategy. The number must be greater than 1
	 * @return sizeOfTheShard
	 */
	public abstract int getShardSize();
	
	/**
	 * Compute what shard i.e. subgraph this Vertex should be added to
	 * @param vertex
	 * @return subgraphid
	 */
	public abstract long getGraphId(String id, String label, List<String> adjacentVertices);
	
	/**
	 * From the supplied list of shards, which shards are full and should be marked as readonly
	 * @param shards
	 * @return list of shards to be marked readonly
	 */
	public abstract List<VSubGraph> listFullShards(Collection<VSubGraph> shards);
	
	/**
	 * Get how frequntly should the flush loop run
	 * @return sleepTime in ms
	 */
	public abstract long flushFrequency();

}
