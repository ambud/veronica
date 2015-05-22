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
package org.veronica.core.configuration;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

public class TestConfigurationManager {

	@Test
	public void testCustomConfigLoad() {
		try {
			System.setProperty(ConfigurationManager.ENV_VERONICA_CONFIG
					, TestConfigurationManager.class.getResource("/config/test-config.properties").toURI().toASCIIString());
			ConfigurationManager configurationManager = ConfigurationManager.getInstance();
			assertNotNull(configurationManager);
			String version = configurationManager.getRawConfig()
					.getString(ConfigurationManager.VERONICA_VERSION);
			System.err.println("version number:"+version);
			assertNotNull(version);
		} catch (ConfigurationException | URISyntaxException e) {
			fail("Configuration manager cannot succeed without a default config path");
		}
	}
	
	@Test
	public void testConstructorFail() {
		try {
			ConfigurationManager configurationManager = ConfigurationManager.getInstance();
			assertNotNull(configurationManager);
			String version = configurationManager.getRawConfig()
					.getString(ConfigurationManager.VERONICA_VERSION);
			System.err.println("version number:"+version);
			assertNotNull(version);
			assertEquals("0.0.1", version);
		} catch (ConfigurationException | URISyntaxException e) {
			fail("Configuration manager cannot succeed without a default config path");
		}
	}
	
}
