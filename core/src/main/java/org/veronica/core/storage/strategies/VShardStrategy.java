package org.veronica.core.storage.strategies;

import java.util.Collection;
import java.util.List;

import org.veronica.core.structures.VSubGraph;
import org.veronica.core.structures.VVertex;

/**
 * This Strategy family to decide how sharding of graph should be handled by Veronica.
 * 
 * A combination of flush and shard strategies should be used to get optimal performance.
 * 
 * @author ambudsharma
 *
 */
public abstract class VShardStrategy extends VStorageStrategy {
	
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
	public abstract String getGraphId(String id, String label, List<String> adjacentVertices);
	
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
