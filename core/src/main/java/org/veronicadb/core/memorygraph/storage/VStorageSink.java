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

import org.apache.commons.configuration.Configuration;
import org.veronicadb.core.memorygraph.VGlobalGraph;
import org.veronicadb.core.memorygraph.VSubGraph;

/**
 * Defines how to physically store graphs in Veronica. 
 * 
 * The sink will abstract the Veronica Graph Database from:
 * 	- storage specific performance tuning
 *  - storage formats and file structures suited for the format
 * 
 * A sink I/Os graph shards in form of {@link VSubGraph} i.e. multiple shards together form the 
 * {@link VGlobalGraph} which is the real graph database a user should be interacting with. 
 * 
 * The storage sink allows different implementations for different storage media e.g.
 * Sequential optimized I/O for magnetic drives or random I/O for SSDs to get maximum performance.
 * 
 * @author ambudsharma
 *
 */
public abstract class VStorageSink {
	
	private String sinkName;
	private Configuration storageConfig;
	private VGlobalGraph graph;

	public VStorageSink(String sinkName, Configuration storageConfig, VGlobalGraph graph) {
		this.sinkName = sinkName;
		this.storageConfig = storageConfig;
		this.graph = graph;
	}
	
	public abstract void init() throws VStorageFailureException;
	
	protected abstract VSubGraph readIndex(String graphId) throws VStorageFailureException;
	
	protected abstract void writeIndex(VSubGraph graphId) throws VStorageFailureException;
	
	public abstract VSubGraph readGraphBlock(String graphId) throws VStorageFailureException;
	
	public abstract long writeGraphBlock(VSubGraph graph) throws VStorageFailureException;
	
	/**
	 * @return the sinkName
	 */
	public String getSinkName() {
		return sinkName;
	}

	/**
	 * @return the storageConfig
	 */
	protected Configuration getStorageConfig() {
		return storageConfig;
	}

	/**
	 * @return the graph
	 */
	protected VGlobalGraph getGraph() {
		return graph;
	}
	
}
