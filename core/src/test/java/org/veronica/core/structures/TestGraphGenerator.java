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

import org.veronica.core.memorygraph.VSubGraph;
import org.veronica.core.memorygraph.storage.GeneratorException;
import org.veronica.core.structures.ShardInitializationException;
import org.veronica.core.structures.VVertex;

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
		try {
			graph.init();
		} catch (ShardInitializationException e) {
			throw new GeneratorException(e);
		}
		AtomicInteger errorCount = new AtomicInteger(0);
		IntStream.range(0, numNodes).forEachOrdered(nodeNumber->{
			VVertex vertex = null;
			try {
				if(randomLabel) {
					vertex = graph.addVertex(String.valueOf(nodeNumber), UUID.randomUUID().toString());
				}else{
					vertex = graph.addVertex(String.valueOf(nodeNumber), null);
				}
				if(vertex==null) {
					throw new Exception("Returned vertex is null");
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorCount.incrementAndGet();
			}
		});
		if(errorCount.get()>0) {
			throw new GeneratorException();
		}
		return graph;
	}

}
