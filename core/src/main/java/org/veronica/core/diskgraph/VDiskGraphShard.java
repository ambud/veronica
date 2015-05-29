package org.veronica.core.diskgraph;

import org.veronica.core.structures.ReadOnlyShardException;
import org.veronica.core.structures.ShardInitializationException;
import org.veronica.core.structures.VGraphShard;
import org.veronica.core.structures.VVertex;

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
