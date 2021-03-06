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
package org.veronicadb.core.memorygraph.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veronicadb.core.memorygraph.VGlobalGraph;
import org.veronicadb.core.memorygraph.VSubGraph;
import org.veronicadb.core.structures.VEdge;
import org.veronicadb.core.structures.VElement;
import org.veronicadb.core.structures.VVertex;

/**
 * This class is the simplest representation of an adjacency list disk read/write of graph shards.
 * 
 * The current implementation is agnostic to SSDs but will perform better on SSDs due to their superior I/O 
 * capabilities.
 * 
 * @author ambudsharma
 */
public class SimpleLocalFileStorageSink extends VStorageSink {
	
	private static final Logger logger = LogManager.getLogger(SimpleLocalFileStorageSink.class);
	private static final String CONF_BASE = "lfss";
	private static final String CONF_STORAGE_DIR = CONF_BASE+".dir";
	private static final String CONF_STORAGE_SSD = CONF_BASE+".ssd";
	private static final int BUFFER_SIZE = 1024*1024;
	
	private String storageDirectoryPathString;
	private File storageDirectory;
	private boolean ssd;
	private boolean compress;
	private Class<? extends CompressorOutputStream> compressor;
	private Class<? extends CompressorInputStream> decompressor;

	public SimpleLocalFileStorageSink(String sinkName, Configuration storageConfig, VGlobalGraph graph) {
		super(sinkName, storageConfig, graph);
	}

	@Override
	public void init() throws VStorageFailureException {
		Configuration config = getStorageConfig();
		// get file storage location
		logger.info("Checking configured storage directory");
		storageDirectoryPathString = config.getString(CONF_STORAGE_DIR, "/tmp/veronica");
		logger.info("Storage configured to:"+storageDirectoryPathString);
		// get is underlying disk is a Solid State Drive (this will have implications on how the flush sizes are computed)
		ssd = config.getBoolean(CONF_STORAGE_SSD, false);
		logger.info("Storage is "+(ssd?"SSD":" not SSD"));
		storageDirectory = new File(storageDirectoryPathString);
		compress = config.getBoolean("compress", false);
		if(compress) {
			compressor = GzipCompressorOutputStream.class;
			decompressor = GzipCompressorInputStream.class;
		}
		
		// perform validations with the storage
		if(!storageDirectory.exists()) {
			throw new VStorageFailureException(this.getClass(), "Storage directory '"+storageDirectory.getAbsolutePath()+"' does not exist", new FileNotFoundException());
		}
		if(!storageDirectory.isDirectory()) {
			throw new VStorageFailureException(this.getClass(), "Storage path '"+storageDirectory.getAbsolutePath()+"' is not a directory", new FileNotFoundException());
		}
		logger.info("Storage directory exists");
		if(!storageDirectory.canRead() || !storageDirectory.canWrite()) {
			throw new VStorageFailureException(this.getClass(), "Need read/write permissions to write to local storage directory:"+storageDirectory.getAbsolutePath());
		}
		logger.info("Found correct permissions on storage directory");
	}

	@Override
	public VSubGraph readIndex(long graphId) throws VStorageFailureException {
		throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Unsupported operation read index");
	}

	@Override
	public void writeIndex(VSubGraph graphId) throws VStorageFailureException {
		throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Unsupported operation write index");
	}

	@SuppressWarnings("resource")
	@Override
	public VSubGraph readGraphBlock(long graphId)
			throws VStorageFailureException {
		logger.info("Read requsted for graphid:"+graphId);
		VSubGraph graph = null;
		File[] files = storageDirectory.listFiles((dir, name)->name.startsWith(graphId+""));
		if(files.length==0) {
			throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Invalid graphId:"+graphId+" can't read block from disk");
		}
		logger.info("Found:"+files.length+" versions of shard for graphid:"+graphId);
		File latestDataFile = Arrays.asList(files)
				.stream()
				.sorted((o1, o2)->Long.compare(o2.lastModified(), o1.lastModified()))
				.findFirst().get();
		logger.info("Latest shard for graphid:"+graphId+" is "+latestDataFile.getName());
		String flushTime = latestDataFile.getName().split("\\.")[0].split("_")[1];
		DataInputStream stream = null;
		InputStream baseStream = null;
		try {
			baseStream = new BufferedInputStream(new FileInputStream(latestDataFile), BUFFER_SIZE);
			if(compress) {
				baseStream = decompressor.getDeclaredConstructor(InputStream.class).newInstance(baseStream);
			}
			stream = new DataInputStream(baseStream);
		} catch (FileNotFoundException e) {
			throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Graph block file doesn't exist for:"+graphId+" file:"+latestDataFile.getPath(), e);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Failed to initialize pluggable de-compressor", e);
		}
		try{
			long readGraphId = readGraphId(stream);
			int vertexCount = stream.readInt();
			byte[] bloomBytes = null;
			if(getGraph()!=null) {
				graph = getGraph().getGraphShard(readGraphId);
				// skip bloom bytes
				skipBloom(stream);
			}else{
				graph = new VSubGraph(readGraphId, vertexCount);
				bloomBytes = readGraphBloom(stream);
			}
			List<VVertex> vertices = readVertices(graph, stream);
			if(getGraph()!=null) {
				graph.loadVertices(vertices);
			}else{
				graph.reinit(bloomBytes, vertices);
			}
			graph.getLastFlush().set(Long.parseLong(flushTime));
		}catch(IOException e) {
			throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Failure to read graphId:"+graphId+" file:"+latestDataFile.getPath()+" from disk", e);
		}finally{
			try {
				stream.close();
				baseStream.close();
			} catch (IOException e) {
				throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Failed to close shard file stream", e);
			}
		}
		return graph;
	}

	/**
	 * Skip bloom bytes if not needed to re-read bloom filter
	 * @param stream
	 * @throws IOException
	 */
	protected void skipBloom(DataInputStream stream) throws IOException {
		int bytesToSkip = stream.readInt();
		stream.skip(bytesToSkip);
	}

	@SuppressWarnings("resource")
	@Override
	public long writeGraphBlock(VSubGraph graph)
			throws VStorageFailureException {
		long time = System.currentTimeMillis();
		long graphId = graph.getGraphId();
		File graphShardFile = new File(storageDirectory, graphId+"_"+time+".vr");
		DataOutputStream stream = null;
		OutputStream baseStream = null;
		try {
			baseStream = new BufferedOutputStream(new FileOutputStream(graphShardFile), BUFFER_SIZE);
			if(compress) {
				baseStream = (OutputStream)compressor.getDeclaredConstructor(OutputStream.class).newInstance(baseStream);
			}
			stream = new DataOutputStream(baseStream);
		} catch (FileNotFoundException e) {
			throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Storage file not found", e);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Failed to initialize pluggable compressor", e);
		} 
		try{
			writeElementId(graphId, stream);
			List<VVertex> vertices = graph.getShardVertices();
			stream.writeInt(vertices.size());
			writeGraphBloom(graph, stream);
			writeVertices(vertices, stream);
		}catch(IOException e) {
			throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Graph block write failed", e);
		}finally{
			try {
				stream.close();
				baseStream.close();
			} catch (IOException e) {
				throw new VStorageFailureException(SimpleLocalFileStorageSink.class, "Failed to close shard file stream", e);
			}
		}
		return time;
	}

	protected void writeVertices(List<VVertex> vertices, DataOutputStream stream)
			throws IOException {
		for (VVertex vertex : vertices) {
			writeElementId(stream, vertex);
			writeElementLabel(stream, vertex);
			writeVertexEdge(stream, vertex);
		}
	}
	
	protected List<VVertex> readVertices(VSubGraph graph, DataInputStream stream) throws IOException {
		List<VVertex> vertices = new ArrayList<VVertex>();
		while(stream.available()>0) {
			long id = readElementId(stream);
			String label = readElementLabel(stream);
			List<VEdge> edges = readVertexEdge(graph, id, stream);
			VVertex vertex = new VVertex(graph, id, label);
			vertex.getEdges().addAll(edges);
			vertices.add(vertex);
		}
		return vertices;
	}

	protected void writeVertexEdge(DataOutputStream stream, VVertex vertex)
			throws IOException {
		stream.writeInt(vertex.getEdges().size());
		for (VEdge edge : vertex.getEdges()) {
			writeElementId(stream, edge);
			writeElementLabel(stream, edge);
			long otherVertex = edge.getOtherVertex(vertex.getId());
			boolean direction = edge.isInV(otherVertex);
			stream.writeBoolean(direction);
			boolean sharedShard = edge.getOutVGraph().getShardId()==edge.getGraphShard().getShardId();
			stream.writeBoolean(sharedShard);
			if(!sharedShard) {
				if(vertex.getGraphShard().getShardId()==edge.getGraphShard().getShardId()) {
					writeElementId(edge.getOutVGraph().getShardId(), stream);
				}else{
					writeElementId(edge.getGraphShard().getShardId(), stream);
				}
			}
			writeElementId(otherVertex, stream);
		}
	}
	
	protected List<VEdge> readVertexEdge(VSubGraph graph, long vertexId, DataInputStream stream) throws IOException {
		int edgeCount = stream.readInt();
		List<VEdge> edges = new ArrayList<VEdge>(edgeCount);
		for(int i=0;i<edgeCount;i++) {
			long edgeId = readElementId(stream);
			String edgeLabel = readElementLabel(stream);
			boolean direction = stream.readBoolean();
			boolean sharedShard = stream.readBoolean();
			VSubGraph graphTwo = null;
			if(sharedShard) {
				graphTwo = graph;
			}else{
				long graphTwoId = readElementId(stream);
				if(getGraph()!=null) {
					graphTwo = getGraph().getGraphShard(graphTwoId);
				}else{
					graphTwo = new VSubGraph(graphTwoId, 1);
				}
			}
			long otherVertexId = readElementId(stream);
			if(direction) {
				VEdge edge = new VEdge(edgeId, edgeLabel, otherVertexId, graphTwo, vertexId, graph);
				edges.add(edge);
			}else{
				VEdge edge = new VEdge(edgeId, edgeLabel, vertexId, graph, otherVertexId, graphTwo);
				edges.add(edge);
			}
		}
		return edges;
	}

	protected void writeElementLabel(DataOutputStream stream, VElement element)
			throws IOException {
		if(element.getLabel()==null) {
			stream.writeInt(-1);
		}else{
			stream.writeInt(element.getLabel().length());
			stream.write(element.getLabel().getBytes());
		}
	}
	
	protected String readElementLabel(DataInputStream stream) throws IOException {
		int val = stream.readInt();
		if(val==-1) {
			return null;
		}else{
			byte[] label = new byte[val];
			stream.readFully(label);
			return new String(label);
		}
	}

	protected void writeElementId(DataOutputStream stream, VElement element)
			throws IOException {
		stream.writeLong(element.getId());
	}
	
	protected long readElementId(DataInputStream stream) throws IOException {
		return stream.readLong();
	}

	protected void writeGraphBloom(VSubGraph graph, DataOutputStream stream)
			throws IOException {
		byte[] bloomBytes = graph.getBloomBytes();
		stream.writeInt(bloomBytes.length);
		stream.write(bloomBytes);
	}
	
	protected byte[] readGraphBloom(DataInputStream stream) throws IOException {
		byte[] bloom = new byte[stream.readInt()];
		stream.readFully(bloom);
		return bloom;
	}

	protected void writeElementId(long id, DataOutputStream stream)
			throws IOException {
		stream.writeLong(id);
	}
	
	protected long readGraphId(DataInputStream stream) throws IOException {
		return stream.readLong();
	}
	
}
