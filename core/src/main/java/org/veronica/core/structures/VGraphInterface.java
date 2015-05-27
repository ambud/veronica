package org.veronica.core.structures;

public interface VGraphInterface {

	public VVertex addVertex(String id, String label) throws InvalidOperationException, ShardInitializationException;
	
	public VVertex removeVertex(String id) throws InvalidOperationException;
	
	public void removeVertex(VVertex vertex) throws InvalidOperationException;
	
}
