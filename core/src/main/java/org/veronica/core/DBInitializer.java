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
package org.veronica.core;

import java.io.File;
import java.util.UUID;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.rocksdb.CompressionType;
import org.rocksdb.HistogramData;
import org.rocksdb.HistogramType;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;

/**
 * Rocks DB tester
 * @author ambudsharma
 *
 */
public class DBInitializer {

	private static final String DB_PATH = "./target/test_rocks";
	private static final int CAP = 100*100*200;

	public DBInitializer() {
	}
	
	public static void main(String[] args) {
		try{
			FileUtils.deleteDirectory(new File(DB_PATH));
		}catch(Exception e) {
			e.printStackTrace();
		}
		RocksDB.loadLibrary();
		Options options = new Options()
			.setCreateIfMissing(true)
			.setBytesPerSync(1024*1024*10)
			.setIncreaseParallelism(4)
			.setWriteBufferSize(1024*1024*20)
			.setMaxOpenFiles(9000)
			.setWalSizeLimitMB(100)
			.setAllowMmapWrites(true)
			.setAllowMmapReads(true)
			.setHardRateLimit(0)
			.setUseFsync(false)
			.setAllowOsBuffer(false)
			.setUseAdaptiveMutex(true)
			.setCompressionType(CompressionType.SNAPPY_COMPRESSION);
			
		try {
		    // a factory method that returns a RocksDB instance
			final RocksDB db = RocksDB.open(options, DB_PATH);
			System.out.println("Value size:"+UUID.randomUUID().toString().getBytes().length);
		    long time = System.currentTimeMillis();
		    
		    for(int i=0;i<CAP;i++) {
		    	db.put(longToBytes(i), longToBytes(i));
		    }
		    
		    time = System.currentTimeMillis() - time;
		    System.out.println("Time: "+time+"ms to write:"
		    		+CAP+" values @"+((double)CAP/(time/1000))+" values/s in sequential mode");
//		    db.close();
//		    System.exit(0);
		   
//		    time = System.currentTimeMillis();
//		    IntStream.range(0, CAP).parallel().forEach(i->{
//		    	try {
//					db.put(UUID.randomUUID().toString().getBytes(), "vals".getBytes());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		    });
//		    options.createStatistics();
//		    time = System.currentTimeMillis() - time;
//		    System.out.println("Time: "+time+"ms to write:"
//		    		+CAP+" values @"+((double)CAP/(time*1000))+" values/s in parallel mode");
		    
		    time = System.currentTimeMillis();
		    WriteOptions writeParams = new WriteOptions();
		    writeParams.setDisableWAL(true);
		    writeParams.setSync(false);
			WriteBatch paramWriteBatch = new WriteBatch(1024*1024*10);
			IntStream.range(0, CAP).parallel().forEach(i->{
		    	try {
		    		String val = UUID.randomUUID().toString();
		    		byte[] key = longToBytes(i);
		    		synchronized (paramWriteBatch) {
		    			paramWriteBatch.put(key, (val).getBytes());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    });
			db.write(writeParams , paramWriteBatch);
			time = System.currentTimeMillis() - time;
		    System.out.println("Time: "+time+"ms to write:"
		    		+CAP+" values @"+((double)CAP*(36+8)/(time/1000))+" b/s in batch mode");
		    options.createStatistics();
		    HistogramData perf = options.statisticsPtr().geHistogramData(HistogramType.COMPACTION_TIME);
		    System.out.println("Perf:"+perf.getAverage()+" Median:"+perf.getMedian());
		    
		} catch (RocksDBException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] longToBytes(long l) {
	    byte[] result = new byte[Long.SIZE];
	    for (int i = 7; i >= 0; i--) {
	        result[i] = (byte)(l & 0xFF);
	        l >>= Long.SIZE;
	    }
	    return result;
	}

	public static long bytesToLong(byte[] b) {
	    long result = 0;
	    for (int i = 0; i < Long.SIZE; i++) {
	        result <<= Long.SIZE;
	        result |= (b[i] & 0xFF);
	    }
	    return result;
	}
	
}
