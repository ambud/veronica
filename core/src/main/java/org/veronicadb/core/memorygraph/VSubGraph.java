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
package org.veronicadb.core.memorygraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.veronicadb.core.structures.ReadOnlyShardException;
import org.veronicadb.core.structures.ShardInitializationException;
import org.veronicadb.core.structures.VElement.IdGenerator;
import org.veronicadb.core.structures.VGraphShard;
import org.veronicadb.core.structures.VVertex;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * A SubGraph is the way Veronica performs sharding of the graph. Since a graph can be thought of as a graph of one or more 
 * smaller graphs this theory can be applied to effectively shard the graph database based on a selected sharding strategy.
 * 
 * Sharding is critical to any concurrent database as is the strategy used to shard. Performing sharding in a logical 
 * fashion provides better native performance while keeping Veronica relatively simple to understand.
 * 
 * Deletes are currently not supported.
 * 
 * @author ambudsharma
 *
 */
public class VSubGraph extends VGraphShard {

	private ConcurrentMap<Long, VVertex> shardVertices;
	// bloom filter for the vertices in this subgraph
	private BloomFilter<Long> subGraphBloom;
	
	public VSubGraph(long graphId, int shardSize) {
		super(graphId, shardSize);
		shardVertices = new ConcurrentHashMap<Long, VVertex>();
		subGraphBloom = BloomFilter.create(Funnels.longFunnel(), shardSize, 0.0001);
		isReadOnly().set(false);
	}

	@Override
	public void init() throws ShardInitializationException {
	}
	
	@Override
	public VVertex addVertex(String id, String label) throws ReadOnlyShardException {
		VVertex vertex = new VVertex(this, id, label);
		addVertex(vertex);
		return vertex;
	}
	
	@Override
	protected void addVertex(VVertex vertex) throws ReadOnlyShardException {
		if(isReadOnly().get()) {
			throw new ReadOnlyShardException("This subgraph is readonly");
		}
		synchronized(subGraphBloom) {
			subGraphBloom.put(vertex.getId());
		}
		shardVertices.put(vertex.getId(), vertex);
	}
	
	/**
	 * Lookup hash and return vertex id if one exists
	 * @param vertexId
	 * @return vertexId
	 */
	public VVertex getVertex(long vertexId) {
		return shardVertices.get(vertexId);
	}
	
	public VVertex getVertex(String vertexId) {
		return getVertex(vertexId, null);
	}
	
	public VVertex getVertex(String id, String label) {
		if(label!=null)
			return shardVertices.get(IdGenerator.hash(id+"_"+label));
		else
			return shardVertices.get(IdGenerator.hash(id)); 
	}

	/**
	 * @return the graphId
	 */
	public Long getGraphId() {
		return getShardId();
	}

	/**
	 * @return the shardVertices
	 */
	public List<VVertex> getShardVertices() {
		return new ArrayList<VVertex>(shardVertices.values());
	}
	
	/**
	 * Is vertex present in this subgraph / graph shard
	 * @param vertexId
	 * @return if vertex might be present in this subgraph
	 */
	@Override
	public boolean vertexExists(long vertexId) {
		return subGraphBloom.mightContain(vertexId);
	}
	
	public boolean vertexExists(String vertexId) {
		return subGraphBloom.mightContain(IdGenerator.hash(vertexId));
	}
	
	/**
	 * Re-initialize this shard from external data. Re-initialized shards are read-only by default
	 * @param bloomBytes
	 * @param vertices
	 * @throws IOException
	 */
	public void reinit(byte[] bloomBytes, List<VVertex> vertices) throws IOException {
		loadBloom(bloomBytes);
		loadVertices(vertices);
		isReadOnly().set(false);
	}
	
	/**
	 * Load all vertices from the list to the hashmap
	 * @param vertices
	 */
	public void loadVertices(List<VVertex> vertices) {
		vertices.stream().parallel().forEach(vertex->shardVertices.put(vertex.getId(), vertex));
	}
	
	/**
	 * Re-initialize bloomfilter from bytes
	 * @param bloomBytes
	 * @throws IOException
	 */
	protected void loadBloom(byte[] bloomBytes) throws IOException {
		subGraphBloom = BloomFilter.readFrom(new ByteArrayInputStream(bloomBytes), Funnels.longFunnel());
	}
	
	/**
	 * @return bytes of the Bloom Filter
	 * @throws IOException
	 */
	public byte[] getBloomBytes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1000);
		BloomFilter<Long> clone = null;
		synchronized (subGraphBloom) {
			clone = subGraphBloom.copy();
		}
		clone.writeTo(os);
		return os.toByteArray();
	}
	
	/**
	 * @return if shard is cached into memory
	 */
	public boolean isCached() {
		return shardVertices.size()>0;
	}
	
	/**
	 * Clear this shard of data but indices and bloomfilter will remain in memory
	 */
	public void clearShard() {
		shardVertices.clear();
	}
	
}