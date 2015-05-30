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
package org.veronicadb.core.memorygraph.storage.strategies;

import org.apache.commons.configuration.Configuration;

/**
 * Abstract class to define storage strategies for Veronica's storage engine.
 * 
 * @author ambudsharma
 *
 */
public abstract class VStorageStrategy {

	private Configuration strategyConfig;
	private String strategyName;
	
	public VStorageStrategy(Configuration strategyConfig, String strategyName) {
		this.strategyConfig = strategyConfig;
		this.strategyName = strategyName;
	}

	/**
	 * @return the strategyConfig
	 */
	protected Configuration getStrategyConfig() {
		return strategyConfig;
	}

	/**
	 * @return the strategyName
	 */
	protected String getStrategyName() {
		return strategyName;
	}

}
