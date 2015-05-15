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

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.config.YamlConfiguration;

public class VGraph implements Graph {

	public static enum ConfigType {
		YAML,
		XML,
		PROP
	};
	
	private final Configuration config;
	
	public VGraph() {
		config = new SystemConfiguration();
	}
	
	public VGraph(ConfigType type, String configPath) throws ConfigurationException {
		switch (type) {
		case YAML:config = new YamlConfiguration(configPath);
			break;
		case PROP:config = new PropertiesConfiguration(configPath);
			break;
		case XML:config = new XMLConfiguration(configPath);
			break;
		default:throw new ConfigurationException("Invalid configuration type");
		}
		startGraphDb();
	}
	
	protected void startGraphDb() {
		
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Vertex addVertex(Object... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphComputer compute() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C extends GraphComputer> C compute(Class<C> arg0)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Configuration configuration() {
		return config;
	}

	@Override
	public Iterator<Edge> edges(Object... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction tx() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Variables variables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Vertex> vertices(Object... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
