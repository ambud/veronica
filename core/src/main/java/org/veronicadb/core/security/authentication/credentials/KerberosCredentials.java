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
 * Kerberos credentials involve a principal name and path to Keytab file to be used for authentication.
 * 
 * @author ambudsharma
 *
 */
public class KerberosCredentials implements VSecurityCredential {

	private String principal;
	private String keytabPath;
	
	public KerberosCredentials(String principal, String keytabPath) {
		this.principal = principal;
		this.keytabPath = keytabPath;
	}

	@Override
	public String getUsername() {
		return principal;
	}

	@Override
	public String getSecret() {
		return keytabPath;
	}

}
