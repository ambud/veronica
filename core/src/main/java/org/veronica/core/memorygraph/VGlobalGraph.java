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
package org.veronica.core.memorygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.veronica.core.memorygraph.storage.VStorageFailureException;
import org.veronica.core.memorygraph.storage.VStorageSink;
import org.veronica.core.memorygraph.storage.strategies.VShardStrategy;
import org.veronica.core.structures.InvalidOperationException;
import org.veronica.core.structures.ReadOnlyShardException;
import org.veronica.core.structures.ShardInitializationException;
import org.veronica.core.structures.VGraphInterface;
import org.veronica.core.structures.VGraphShard;
import org.veronica.core.structures.VVertex;

/**
 * The actual graph database implementation of Veronica. The global graph is built from one or more {@link VSubGraph}
 * each containing a shard of vertices and their corresponding edges.
 * 
 * @author ambudsharma
 *
 */
public class VGlobalGraph implements VGraphInterface {
	
	private static final long serialVersionUID = -5422866707283657373L;
	private ConcurrentMap<String, VSubGraph> graphShardHash;
	private VStorageSink sink;
	private VShardStrategy shardingStrategy;
	private ScheduledExecutorService backgroundService;
	
	private VGlobalGraph() {
		graphShardHash = new ConcurrentHashMap<String, VSubGraph>();
		backgroundService = Executors.newScheduledThreadPool(1);
	}
	
	public VGlobalGraph(VStorageSink sink, VShardStrategy shardingStrategy) {
		this();
		this.sink = sink;
		this.shardingStrategy = shardingStrategy;
	}

	public void init() {
		backgroundService.scheduleAtFixedRate(()->{
			List<VSubGraph> fullShards = shardingStrategy.listFullShards(graphShardHash.values());
			fullShards.stream().parallel().forEach(shard->{
				shard.isReadOnly().set(true);
				try {
					long ts = sink.writeGraphBlock(shard);
					shard.getLastFlush().set(ts);
					shard.clearShard();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}, 10000, shardingStrategy.flushFrequency(), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * @param vertex
	 * @throws InvalidOperationException
	 * @throws ShardInitializationException 
	 */
	public VVertex addVertex(String id, String label) throws InvalidOperationException, ShardInitializationException {
		String graphId = shardingStrategy.getGraphId(id, label, null);
		if(!graphShardHash.containsKey(graphId)) {
			VSubGraph subGraph = new VSubGraph(graphId, shardingStrategy.getShardSize());
			subGraph.init();
			graphShardHash.put(graphId, subGraph);
		}
		try {
			return graphShardHash.get(graphId).addVertex(id, label);
		} catch (ReadOnlyShardException e) {
			throw new InvalidOperationException(e.getMessage());
		}
	}
	
	/**
	 * Parallel find a get vertex from the shards
	 * @param vertexId
	 * @return vertex if it exits else null
	 */
	public VVertex getVertex(String vertexId) {
		List<VSubGraph> subGraphs = graphShardHash.values()
		.stream().parallel()
		.filter(subGraph->subGraph.vertexExists(vertexId))
		.collect(Collectors.toList());
		if(subGraphs.size()>0) {
			Stream<VVertex> val = subGraphs.parallelStream().filter(subGraph->!subGraph.isCached()).map(subGraph->subGraph.getVertex(vertexId)).filter(vertex->vertex!=null);
			VVertex result = val.limit(1).findFirst().get();
			if(result==null) {
				return subGraphs.stream().filter(subGraph->!subGraph.isCached())
					.map(subGraph->warmSubGraph(subGraph))
					.map(subGraph->subGraph.getVertex(vertexId))
					.filter(vertex->vertex!=null)
					.limit(1)
					.findFirst().get();
			}else{
				return result;
			}
		}else{
			return null;
		}
	}

	protected VSubGraph warmSubGraph(VSubGraph subGraph) {
		try {
			sink.readGraphBlock(subGraph.getGraphId());
		} catch (VStorageFailureException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Lookup graph shard by id
	 * @param graphId
	 * @return graph shard object
	 */
	@Override
	public VSubGraph getGraphShard(String graphId) {
		return graphShardHash.get(graphId);
	}
	
	@Override
	public VVertex removeVertex(String id) throws InvalidOperationException {
		throw new InvalidOperationException("Global graph implementation does not currently support removing vertices");
	}

	@Override
	public void removeVertex(VVertex vertex) throws InvalidOperationException {
		throw new InvalidOperationException("Global graph implementation does not currently support removing vertices");
	}

	@Override
	public List<VGraphShard> getShards() {
		return new ArrayList<VGraphShard>(graphShardHash.values());
	}

}
