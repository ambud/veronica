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

import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.veronica.core.configuration.ConfigurationManager;


/**
 * Abstract element of the graph with common properties that apply to both Vertices and Edges.
 * 
 * @author ambudsharma
 */
public abstract class VElement {
	
	private Map<String, Object> propertyMap;
	private String key;
	private String id;
	private String label;
	private VSubGraph graph;
	
	public VElement(VSubGraph graph, String id, String label) {
		this.graph = graph;
		this.id = id;
		this.label = label;
		this.propertyMap = new ConcurrentHashMap<String, Object>();
		computeId();
	}
	
	public VElement(VSubGraph graph, String id, String label, String key) {
		this(graph, id, label);
		this.key = key;
	}
	
	protected void computeId() {
		if(id==null) {
			id = IdGenerator.hashSHA1(UUID.randomUUID().toString());
		}else{
			if(label!=null) {
				id = IdGenerator.hashSHA1(label+"_"+id);
			}else {
				id = IdGenerator.hashSHA1(id);
			}
		}
	}

	/**
	 * @param label the label to set
	 */
	protected void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Put property into the element. 
	 * 
	 * If the key property has been initialized; this will intercept and recompute the element id 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void put(String propertyName, Object propertyValue) {
		if(key!=null && key.equalsIgnoreCase(propertyName)) {
			id = propertyValue.toString();
			computeId();
		}
		propertyMap.put(propertyName, propertyValue);
	}

	/**
	 * @return the propertyMap
	 */
	public Map<String, Object> getPropertyMap() {
		return propertyMap;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the key
	 */
	protected String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	protected void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the graph
	 */
	protected VSubGraph getGraph() {
		return graph;
	}

	public static class IdGenerator {
		
		private static int hashAlgo;
		static {
			try {
				String algo = ConfigurationManager.getInstance().getConfig().getString("id.algorithm", "sha1");
				switch(algo) {
				case "sha1": hashAlgo = 0;
					break;
				case "md5": hashAlgo = 1;
					break;
				case "sha256": hashAlgo = 2;
					break;
				default: hashAlgo = -1;
				}
			} catch (ConfigurationException | URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}
		
		private IdGenerator() {
		}
		
		public static String hashSHA1(String id) {
			if(hashAlgo == 0) {
				return DigestUtils.sha1Hex(id);
			}else if(hashAlgo == 1) {
				return DigestUtils.md5Hex(id);
			}else if(hashAlgo == 2){
				return DigestUtils.sha256Hex(id);
			}else {
				return id;
			}
		}
		
	}
}
