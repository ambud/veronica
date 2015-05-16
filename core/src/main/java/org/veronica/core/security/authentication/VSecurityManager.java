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
package org.veronica.core.security.authentication;

/**
 * Security Manager class for the database. It's a singleton and is responsible for managing the security plugins for the graph database.
 * @author ambudsharma
 *
 */
public class VSecurityManager {
	
	private static VSecurityManager self = new VSecurityManager();
	
	/**
	 * Constructor for the security manager
	 */
	private VSecurityManager() {
		init();
	}
	
	/**
	 * @return instance of the security manager
	 */
	public static VSecurityManager getInstance() {
		return self;
	}
	
	/**
	 * Initializes the security manager
	 */
	protected void init() {
		
	}

}
