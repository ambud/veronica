package org.veronica.core.structures;

import java.io.Serializable;
import java.util.List;

public interface VGraphInterface extends Serializable {

	public VVertex addVertex(String id, String label) throws InvalidOperationException, ShardInitializationException;
	
	public VVertex removeVertex(String id) throws InvalidOperationException;
	
	public void removeVertex(VVertex vertex) throws InvalidOperationException;
	
	public List<VGraphShard> getShards();
	
	public VGraphShard getGraphShard(String shardId);
	
}
