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
package org.veronica.core.storage;

import org.veronica.core.VException;

public class VStorageFailureException extends VException {

	private static final long serialVersionUID = 1L;
	
	public VStorageFailureException(Class<? extends VStorageSink> from, String message, Throwable e) {
		super("Storage failure:"+from.getCanonicalName()+":"+message, e);
	}

	public VStorageFailureException(
			Class<? extends LocalFileStorageSink> from, String message) {
		super("Storage failure:"+from.getCanonicalName()+":"+message);
	}

}
