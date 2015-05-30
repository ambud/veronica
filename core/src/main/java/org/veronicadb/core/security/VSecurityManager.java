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
package org.veronicadb.core.security;

import java.net.URISyntaxException;

import org.apache.commons.configuration.ConfigurationException;
import org.veronicadb.core.configuration.ConfigurationManager;

/**
 * Security Manager class for the database. It's a singleton and is responsible for managing the security plugins for the graph database.
 * 
 * @author ambudsharma
 */
public class VSecurityManager {
	
	private static VSecurityManager self;
	
	/**
	 * Constructor for the security manager
	 * @throws URISyntaxException 
	 * @throws ConfigurationException 
	 */
	private VSecurityManager() throws ConfigurationException, URISyntaxException {
		init();
	}
	
	/**
	 * @return instance of the security manager
	 * @throws URISyntaxException 
	 * @throws ConfigurationException 
	 */
	public static VSecurityManager getInstance() throws ConfigurationException, URISyntaxException {
		if(self==null) {
			self = new VSecurityManager();
		}
		return self;
	}
	
	/**
	 * Initializes the security manager
	 * @throws Exception 
	 * @throws ConfigurationException 
	 * @throws URISyntaxException 
	 */
	protected void init() throws ConfigurationException, URISyntaxException {
		ConfigurationManager.getInstance();
	}

}
