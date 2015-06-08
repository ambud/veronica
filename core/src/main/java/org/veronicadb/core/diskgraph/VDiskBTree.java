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
package org.veronicadb.core.diskgraph;

import java.nio.MappedByteBuffer;

public class VDiskBTree {
	
	private MappedByteBuffer vertexIndex;
	
	
	public void add(long key, long value) {
		
	}
	
	private class Node {
		
		private long[] keys = null;
		private Entry[] children = null;
		
		public Node(int keySize) {
			keys = new long[keySize];
			children = new Entry[keySize+1];
		}
		
	}
	
	private class Entry {
		
		private long key;
		private long value;
		
		public Entry(long key, long value) {
			this.key = key;
			this.value = value;
		}
		
	}

}
