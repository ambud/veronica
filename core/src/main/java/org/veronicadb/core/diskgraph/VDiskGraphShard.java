package org.veronicadb.core.diskgraph;

import org.veronicadb.core.structures.ReadOnlyShardException;
import org.veronicadb.core.structures.ShardInitializationException;
import org.veronicadb.core.structures.VGraphShard;
import org.veronicadb.core.structures.VVertex;

public class VDiskGraphShard extends VGraphShard implements Runnable {

	public VDiskGraphShard(String shardId, int shardSize) {
		super(shardId, shardSize);
	}

	@Override
	public void init() throws ShardInitializationException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public VVertex addVertex(String id, String label)
			throws ReadOnlyShardException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addVertex(VVertex vertex) throws ReadOnlyShardException {
		// TODO Auto-generated method stub

	}

	@Override
	public VVertex getVertex(String vertexId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean vertexExists(String vertexId) {
		// TODO Auto-generated method stub
		return false;
	}

}
