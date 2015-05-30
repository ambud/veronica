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
package org.veronicadb.core.security.authentication.credentials;

import org.veronicadb.core.security.authentication.VSecurityCredential;

/**
 * Simple username and password based credentials
 * 
 * @author ambudsharma
 */
public class SimpleUsernamePasswordCredentials implements VSecurityCredential {

	private String username;
	private String password;
	
	public SimpleUsernamePasswordCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getSecret() {
		return password;
	}

}
