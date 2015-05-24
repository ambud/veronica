package org.veronica.core.structures;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TestGraphGenerator {
	
	private TestGraphGenerator() {
	}
	
	public static void recurrsiveDeleteDirectory(String path) throws IOException {
		Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
			@Override
			
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	public static VSubGraph generateContinuousGraph(int numNodes, boolean randomLabel) throws GeneratorException {
		VSubGraph graph = new VSubGraph(UUID.randomUUID().toString(), numNodes);
		AtomicInteger errorCount = new AtomicInteger(0);
		IntStream.range(0, numNodes).forEachOrdered(nodeNumber->{
			VVertex vertex = null;
			if(randomLabel) {
				vertex = new VVertex(graph, String.valueOf(nodeNumber), UUID.randomUUID().toString());
			}else{
				vertex = new VVertex(graph, null, null);
			}
			try {
				graph.addVertex(vertex);
			} catch (Exception e) {
				errorCount.incrementAndGet();
			}
		});
		if(errorCount.get()>0) {
			throw new GeneratorException();
		}
		return graph;
	}

}
