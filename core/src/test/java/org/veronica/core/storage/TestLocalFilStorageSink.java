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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.veronica.core.structures.GeneratorException;
import org.veronica.core.structures.TestGraphGenerator;
import org.veronica.core.structures.VSubGraph;

public class TestLocalFilStorageSink {

	private static final Logger logger = LogManager.getLogger(TestLocalFilStorageSink.class);
	private static final String TARGET_VERONICA_DIRECTORY = "./target/veronica";
	private static Configuration storageConfig;
	
	@BeforeClass
	public static void before() {
		logger.error("Building default configuration for test");
		storageConfig = new PropertiesConfiguration();
		new File(TARGET_VERONICA_DIRECTORY).mkdirs();
		logger.error("Created target storage directories");
		storageConfig.setProperty("lfss.dir", TARGET_VERONICA_DIRECTORY);
		storageConfig.setProperty("lfss.ssd", true);
	}
	
	@AfterClass
	public static void after() throws IOException {
		TestGraphGenerator.recurrsiveDeleteDirectory(TARGET_VERONICA_DIRECTORY);
		logger.error("Deleted target storage directories");
	}
	
	@Test
	public void testInit() {
		VStorageSink sink = new LocalFileStorageSink("local-ssd", storageConfig);
		try {
			sink.init();
		} catch (VStorageFailureException e) {
			fail("Failed to initialize local storage:"+e.getMessage());
		}
	}
	
	@Test
	public void testSampleWrite() throws GeneratorException {
		VSubGraph graph = TestGraphGenerator.generateContinuousGraph(10, true);
		VStorageSink sink = new LocalFileStorageSink("local-ssd", storageConfig);
		try {
			sink.init();
			sink.writeGraphBlock(graph);
		} catch (VStorageFailureException e) {
			fail("Storage exception:"+e.getMessage());
		}
	}
	
	@Test
	public void testSampleRead() throws GeneratorException {
		VStorageSink sink = new LocalFileStorageSink("local-ssd", storageConfig);
		try {
			VSubGraph graph = TestGraphGenerator.generateContinuousGraph(10, true);
			sink.init();
			sink.writeGraphBlock(graph);
			graph = sink.readGraphBlock(graph.getGraphId());
			assertNotNull(graph);
			assertEquals(10, graph.getShardVertices().size());
		} catch (VStorageFailureException e) {
			fail("Storage exception:"+e.getMessage());
		}
	}
	
}
