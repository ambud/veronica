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

import org.veronica.core.security.authentication.credentials.KerberosCredentials;
import org.veronica.core.security.authentication.credentials.SimpleUsernamePasswordCredentials;

/**
 * Inspired by Hadoop's UserGroupInformation class, the VSecurityManager is a Security Manager for Veronica.
 * 
 * 
 * 
 * @author ambudsharma
 *
 */
public class VSecurityContext {
	
	public VSecurityContext() {
	}
	
	public static VSecurityContext createSecurityContext(VSecurityCredential credentials) {
		if(credentials instanceof SimpleUsernamePasswordCredentials) {
			
		}else if(credentials instanceof KerberosCredentials) {
			
		}
		return new VSecurityContext();
	}
	
}
