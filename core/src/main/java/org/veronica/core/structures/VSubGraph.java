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
package org.veronica.core.structures;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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
public class VSubGraph {

	private String graphId;
	private ConcurrentMap<String, VVertex> shardVertices;
	// bloom filter for the vertices in this subgraph
	private BloomFilter<CharSequence> subGraphBloom;
	private AtomicBoolean readOnly = new AtomicBoolean(true);
	private AtomicLong lastFlush = new AtomicLong(-1);
	
	public VSubGraph(String graphId, int shardSize) {
		this.graphId = graphId;
		shardVertices = new ConcurrentHashMap<String, VVertex>();
		subGraphBloom = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), shardSize, 0.0001);
		readOnly.set(false);
	}
	
	/**
	 * Add vertex with id and label information only
	 * @param id
	 * @param label
	 * @return vertex
	 * @throws ReadOnlyShardException 
	 */
	protected VVertex addVertex(String id, String label) throws ReadOnlyShardException {
		VVertex vertex = new VVertex(this, id, label);
		addVertex(vertex);
		return vertex;
	}
	
	/**
	 * Add a vertex to this shard
	 * @param vertex
	 * @throws ReadOnlyShardException
	 */
	private void addVertex(VVertex vertex) throws ReadOnlyShardException {
		if(readOnly.get()) {
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
	protected VVertex getVertex(String vertexId) {
		return shardVertices.get(vertexId);
	}

	/**
	 * @return the graphId
	 */
	public String getGraphId() {
		return graphId;
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
	public boolean vertexExist(String vertexId) {
		return subGraphBloom.mightContain(vertexId);
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
		readOnly.set(false);
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
		subGraphBloom = BloomFilter.readFrom(new ByteArrayInputStream(bloomBytes), Funnels.stringFunnel(Charset.defaultCharset()));
	}
	
	/**
	 * @return bytes of the Bloom Filter
	 * @throws IOException
	 */
	public byte[] getBloomBytes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1000);
		BloomFilter<CharSequence> clone = null;
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

	/**
	 * @return the lastFlush timestamp
	 */
	public AtomicLong getLastFlush() {
		return lastFlush;
	}

	/**
	 * @return the readOnly
	 */
	protected AtomicBoolean isReadOnly() {
		return readOnly;
	}
	
}
