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

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.configuration.Configuration;

/**
 * 
 * @author ambudsharma
 */
public abstract class LocalFileStorageSink extends VStorageSink {
	
	private static final String CONF_BASE = "lfss";
	private static final String CONF_STORAGE_DIR = CONF_BASE+".dir";
	private static final String CONF_STORAGE_SSD = CONF_BASE+".ssd";
	
	private String storageDirectoryPathString;
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
		File storageDirectory = new File(storageDirectoryPathString);
		
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
	
	
}
