package org.veronica.core.structures;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public abstract class VGraphShard {

	protected final String shardId;
	protected final int shardSize;
	private AtomicLong lastFlush = new AtomicLong(-1);
	private AtomicBoolean readOnly = new AtomicBoolean(false);
	
	public VGraphShard(String shardId, int shardSize) {
		this.shardId = shardId;
		this.shardSize = shardSize;
	}
	
	public abstract void init() throws ShardInitializationException;
	
	/**
	 * Add vertex with id and label information only
	 * @param id
	 * @param label
	 * @return vertex
	 * @throws ReadOnlyShardException 
	 */
	public abstract VVertex addVertex(String id, String label) throws ReadOnlyShardException;
	
	/**
	 * Add a vertex to this shard
	 * @param vertex
	 * @throws ReadOnlyShardException
	 */
	protected abstract void addVertex(VVertex vertex) throws ReadOnlyShardException;

	public abstract VVertex getVertex(String vertexId);
	
	public abstract boolean vertexExists(String vertexId);
	/**
	 * @return the shardId
	 */
	public String getShardId() {
		return shardId;
	}

	/**
	 * @return the shardSize
	 */
	public int getShardSize() {
		return shardSize;
	}

	/**
	 * @return the lastFlush
	 */
	public AtomicLong getLastFlush() {
		return lastFlush;
	}

	/**
	 * @return the readOnly
	 */
	public AtomicBoolean isReadOnly() {
		return readOnly;
	}
	
}
