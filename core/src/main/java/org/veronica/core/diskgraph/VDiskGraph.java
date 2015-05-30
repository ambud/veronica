package org.veronica.core.diskgraph;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.veronica.core.diskgraph.operations.AbstractGraphOperation;
import org.veronica.core.structures.InvalidOperationException;
import org.veronica.core.structures.ShardInitializationException;
import org.veronica.core.structures.VGraphInterface;
import org.veronica.core.structures.VGraphShard;
import org.veronica.core.structures.VVertex;

/**
 * 
 * The disk graph will be transactional i.e. strict transactions can be maintained using the disk graph.
 * 
 * @author ambudsharma
 */
public class VDiskGraph implements VGraphInterface {

	private static final long serialVersionUID = 1337611520673518906L;
	private ArrayBlockingQueue<AbstractGraphOperation> operationQueue;
	
	public VDiskGraph(int opsQueueCapacity) {
		 operationQueue = new ArrayBlockingQueue<AbstractGraphOperation>(opsQueueCapacity, true);
	}

	@Override
	public VVertex addVertex(String id, String label)
			throws InvalidOperationException, ShardInitializationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VVertex removeVertex(String id) throws InvalidOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeVertex(VVertex vertex) throws InvalidOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<VGraphShard> getShards() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VGraphShard getGraphShard(String shardId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getOperationsBacklog() {
		return operationQueue.size();
	}

}
