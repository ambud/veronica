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
package org.veronica.core.server.protocol;

import java.util.ArrayList;
import java.util.List;

import org.veronica.core.structures.VElement;

public abstract class VGraphOperation {

	protected byte operationCode;
	private List<VElement> element;
	
	public VGraphOperation() {
		element = new ArrayList<VElement>();
	}

	public VGraphOperation(byte operationCode, List<VElement> element) {
		this.operationCode = operationCode;
		this.element = element;
	}

	public List<VElement> getElement() {
		return element;
	}

	public byte getOperationCode() {
		return operationCode;
	}
	
	public int getOperationCount() {
		return element!=null?element.size():0;
	}
	
}
