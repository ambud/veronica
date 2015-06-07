package org.veronicadb.core.diskgraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.veronicadb.core.structures.ReadOnlyShardException;
import org.veronicadb.core.structures.ShardInitializationException;
import org.veronicadb.core.structures.VGraphShard;
import org.veronicadb.core.structures.VVertex;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class VDiskGraphShard extends VGraphShard {
	
	private static final String CONFIG_STORAGE_DIRECTORY = "./target";
	private LinkedHashMap<String, VVertex> cache;
	private Map<Long, Long> vertexIndexHash;
	private BloomFilter<Long> shardBloom;
	private Queue<RandomAccessFile> fileReadPointers;
	private Set<Long> elementLocks;
	private String shardFileLocation;
	private MappedByteBuffer vertexIndex;
	private FileChannel indexChannel;
	
	public VDiskGraphShard(long shardId, int shardSize) {
		super(shardId, shardSize);
		this.shardBloom = BloomFilter.create(Funnels.longFunnel(), shardSize, 0.0001);
		this.cache = new LinkedHashMap<String, VVertex>((int)(shardSize/0.60), 0.75f, true);
		this.shardFileLocation = CONFIG_STORAGE_DIRECTORY+"/"+shardId;
		this.fileReadPointers = new ArrayBlockingQueue<RandomAccessFile>(10, true);
		this.elementLocks = new HashSet<Long>();
		this.vertexIndexHash = new ConcurrentHashMap<Long, Long>(shardSize);
	}

	@Override
	public void init() throws ShardInitializationException {
		File shardFile = new File(shardFileLocation+".dat");
		File indexFile = new File(shardFileLocation+".idx");
		if(!shardFile.exists()) {
			try {
				shardFile.createNewFile();
			} catch (IOException e) {
				throw new ShardInitializationException(e);
			}
		}
		if(!indexFile.exists()) {
			try {
				indexFile.createNewFile();
			} catch (IOException e) {
				throw new ShardInitializationException(e);
			}
		}
		try {
			this.indexChannel = new RandomAccessFile(indexFile, "rw").getChannel();
			this.vertexIndex = this.indexChannel.map(MapMode.READ_WRITE, 0, this.indexChannel.size());
		} catch (IOException e) {
			throw new ShardInitializationException(e);
		}
		for(int i=0;i<fileReadPointers.size();i++) {
			RandomAccessFile filePointer = null;
			try {
				filePointer = new RandomAccessFile(shardFile, "r");
			} catch (FileNotFoundException e) {
				throw new ShardInitializationException(e);
			}
			fileReadPointers.add(filePointer);
		}
	}
	
	@Override
	public VVertex addVertex(String id, String label)
			throws ReadOnlyShardException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addVertex(VVertex vertex) throws ReadOnlyShardException {
	}
	
	public void updateElement() {
		// put element id in the locks set
		
	}

	@Override
	public VVertex getVertex(long vertexId) throws IOException {
		if(!vertexExists(vertexId)) {
			return null;
		}else{
			if(cache.containsKey(vertexId)) {
				return cache.get(vertexId);
			}else{
				// put data in the cache
				RandomAccessFile raf = fileReadPointers.poll();
				FileChannel channel = raf.getChannel();
				Long position = vertexIndexHash.get(vertexId);
				try {
					channel.position(position);
					ByteBuffer buffer = ByteBuffer.allocate(4);
					channel.read(buffer);
					int length = buffer.getInt();
					buffer = ByteBuffer.allocate(length);
					int next = 0;
					do {
						next = buffer.getInt();
					}while(next!=-1);
					
				} catch (IOException e) {
					throw e;
				}
				return null;
			}
		}
	}

	@Override
	public boolean vertexExists(long vertexId) {
		if(shardBloom.mightContain(vertexId)) {
			if(cache.containsKey(vertexId)) {
				return true;
			}else{
				// actual file lookup
				return false;
			}
		}else{
			return false;
		}
	}

}
