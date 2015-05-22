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

import org.apache.commons.configuration.Configuration;
import org.veronica.core.structures.VSubGraph;

/**
 * Define how to physically store graphs in Veronica. 
 * 
 * The sink will abstract the Veronica Graph Database from:
 * 	- storage specific performance tuning
 *  - storage formats and file structures suited for the format
 *   
 * @author ambudsharma
 *
 */
public abstract class VStorageSink {
	
	private String sinkName;
	private Configuration storageConfig;

	public VStorageSink(String sinkName, Configuration storageConfig) {
		this.sinkName = sinkName;
		this.storageConfig = storageConfig;
	}
	
	public abstract void init() throws VStorageFailureException;
	
	public abstract void readIndex(String graphId) throws VStorageFailureException;
	
	public abstract void writeIndex(String graphId) throws VStorageFailureException;
	
	public abstract VSubGraph readGraphBlock(String graphId) throws VStorageFailureException;
	
	public abstract void writeGraphBlock(VSubGraph graph) throws VStorageFailureException;
	
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
	
}
