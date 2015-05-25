package org.veronica.core.storage;

import static org.junit.Assert.*;

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

public class TestSimpleLocalFileStoragePerf {

	private static final Logger logger = LogManager.getLogger(TestSimpleLocalFileStoragePerf.class);
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
	public void testHeavyVertexWrite() throws GeneratorException {
		long time = System.currentTimeMillis();
		VSubGraph graph = TestGraphGenerator.generateContinuousGraph(100000, true);
		VStorageSink sink = new SimpleLocalFileStorageSink("local-ssd", storageConfig, null);
		try {
			sink.init();
			sink.writeGraphBlock(graph);
		} catch (VStorageFailureException e) {
			fail("Storage exception:"+e.getMessage());
		}
		time = System.currentTimeMillis() - time;
		logger.warn("Perf time to write graph of 100K :"+time+"ms");
	}
	
	@Test
	public void testHeavyVertexLoad() throws GeneratorException {
		long time = System.currentTimeMillis();
		VSubGraph graph = TestGraphGenerator.generateContinuousGraph(100000, true);
		VStorageSink sink = new SimpleLocalFileStorageSink("local-ssd", storageConfig, null);
		try {
			sink.init();
			long ts = sink.writeGraphBlock(graph);
			long bytes = new File(TARGET_VERONICA_DIRECTORY+"/"+graph.getGraphId()+"_"+ts+".vr").length();
			logger.info("Written file:"+((double)bytes)/(1024*1024)+" MB");
			VSubGraph graphBlock = sink.readGraphBlock(graph.getGraphId());
			assertNotNull(graphBlock);
		} catch (VStorageFailureException e) {
			fail("Storage exception:"+e.getMessage());
		}
		time = System.currentTimeMillis() - time;
		logger.warn("Perf time to read/write graph of 100K :"+time+"ms");
	}
	
}