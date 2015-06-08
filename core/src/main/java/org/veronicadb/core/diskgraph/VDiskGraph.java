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
package org.veronicadb.core.diskgraph;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.veronicadb.core.diskgraph.operations.AbstractGraphOperation;
import org.veronicadb.core.structures.InvalidOperationException;
import org.veronicadb.core.structures.ShardInitializationException;
import org.veronicadb.core.structures.VGraphInterface;
import org.veronicadb.core.structures.VGraphShard;
import org.veronicadb.core.structures.VVertex;

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
	public VGraphShard getGraphShard(long shardId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getOperationsBacklog() {
		return operationQueue.size();
	}

}
