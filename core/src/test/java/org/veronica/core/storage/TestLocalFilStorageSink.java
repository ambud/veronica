package org.veronica.core.storage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;

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
