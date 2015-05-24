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
package org.veronica.core.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.veronica.core.structures.VEdge;
import org.veronica.core.structures.VElement;
import org.veronica.core.structures.VSubGraph;
import org.veronica.core.structures.VVertex;

/**
 * 
 * @author ambudsharma
 */
public class LocalFileStorageSink extends VStorageSink {
	
	private static final String CONF_BASE = "lfss";
	private static final String CONF_STORAGE_DIR = CONF_BASE+".dir";
	private static final String CONF_STORAGE_SSD = CONF_BASE+".ssd";
	
	private String storageDirectoryPathString;
	private File storageDirectory;
	private boolean ssd;

	public LocalFileStorageSink(String sinkName, Configuration storageConfig) {
		super(sinkName, storageConfig);
	}

	@Override
	public void init() throws VStorageFailureException {
		Configuration config = getStorageConfig();
		// get file storage location
		storageDirectoryPathString = config.getString(CONF_STORAGE_DIR, "/tmp/veronica");
		// get is underlying disk is a Solid State Drive (this will have implications on how the flush sizes are computed)
		ssd = config.getBoolean(CONF_STORAGE_SSD, false);
		storageDirectory = new File(storageDirectoryPathString);
		
		// perform validations with the storage
		if(!storageDirectory.exists()) {
			throw new VStorageFailureException(this.getClass(), "Storage directory '"+storageDirectory.getAbsolutePath()+"' does not exist", new FileNotFoundException());
		}
		if(!storageDirectory.isDirectory()) {
			throw new VStorageFailureException(this.getClass(), "Storage path '"+storageDirectory.getAbsolutePath()+"' is not a directory", new FileNotFoundException());
		}
		if(!storageDirectory.canRead() || !storageDirectory.canWrite()) {
			throw new VStorageFailureException(this.getClass(), "Need read/write permissions to write to local storage directory:"+storageDirectory.getAbsolutePath());
		}
	}

	@Override
	public VSubGraph readIndex(String graphId) throws VStorageFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeIndex(VSubGraph graphId) throws VStorageFailureException {
		// TODO Auto-generated method stub
	}

	@Override
	public VSubGraph readGraphBlock(String graphId)
			throws VStorageFailureException {
		
		return null;
	}

	@Override
	public long writeGraphBlock(VSubGraph graph)
			throws VStorageFailureException {
		long time = System.currentTimeMillis();
		String graphId = graph.getGraphId();
		File graphShardFile = new File(storageDirectory, graphId+"-"+time+".vr");
		DataOutputStream stream = null;
		try {
			stream = new DataOutputStream(new FileOutputStream(graphShardFile));
		} catch (FileNotFoundException e) {
			throw new VStorageFailureException(LocalFileStorageSink.class, "Storage file not found", e);
		}
		try{
			writeGraphId(graphId, stream);
			writeGraphBloom(graph, stream);
			writeVertices(graph, stream);
		}catch(IOException e) {
			e.printStackTrace();
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				throw new VStorageFailureException(LocalFileStorageSink.class, "Failed to close shard file stream");
			}
		}
		return time;
	}

	protected void writeVertices(VSubGraph graph, DataOutputStream stream)
			throws IOException {
		List<VVertex> vertices = graph.getShardVertices();
		for (VVertex vertex : vertices) {
			writeElementId(stream, vertex);
			writeElementLabel(stream, vertex);
			writeVertexEdge(stream, vertex);
		}
	}
	
	protected List<VVertex> readVertices(DataInputStream stream) {
		return null;
	}

	protected void writeVertexEdge(DataOutputStream stream, VVertex vertex)
			throws IOException {
		for (VEdge edge : vertex.getEdges()) {
			writeElementId(stream, edge);
			writeElementLabel(stream, edge);
			String otherVertex = edge.getOtherVertex(vertex).getId();
			writeGraphId(otherVertex, stream);
		}
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
	
	protected String readVertexLabel(DataInputStream stream) throws IOException {
		int val = stream.readInt();
		if(val==-1) {
			return null;
		}else{
			byte[] label = new byte[val];
			stream.readFully(label);
			return new String(label);
		}
	}

	protected void writeElementId(DataOutputStream stream,VElement element)
			throws IOException {
		stream.writeInt(element.getId().length());
		stream.write(element.getId().getBytes());
	}
	
	protected String readElementId(DataInputStream stream) throws IOException {
		byte[] vertexId = new byte[stream.readInt()];
		stream.readFully(vertexId);
		return new String(vertexId);
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

	protected void writeGraphId(String graphId, DataOutputStream stream)
			throws IOException {
		stream.writeInt(graphId.length());
		stream.write(graphId.getBytes());
	}
	
	protected String readGraphId(DataInputStream stream) throws IOException {
		byte[] id = new byte[stream.readInt()];
		stream.readFully(id);
		return new String(id);
	}
	
}
