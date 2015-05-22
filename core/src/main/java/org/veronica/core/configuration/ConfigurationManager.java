/*
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

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * Configuration Manager singleton.
 * 
 * @author ambudsharma
 */
public class ConfigurationManager {

	private static final Logger logger = Logger.getLogger(ConfigurationManager.class.getCanonicalName());
	public static final String ENV_VERONICA_CONFIG = "VERONICA_CONFIG";
	private static final String CONFIG_BASE_PREFIX = "org.veronica.";
	private static final String CONFIG_SECURITY_PREFIX = CONFIG_BASE_PREFIX+"security.";
	private static final String CONFIG_STORAGE_PREFIX = CONFIG_BASE_PREFIX+"storage.";
	public static final String VERONICA_VERSION = CONFIG_BASE_PREFIX+"version";
	
	public static enum ConfigType {
		XML,
		PROP
	};
	
	private final Configuration config;
	private static ConfigurationManager self;

	private ConfigurationManager() throws ConfigurationException, URISyntaxException {
		Configuration configuration = loadDefaultConfiguration();
		String configPath = System.getenv(ENV_VERONICA_CONFIG);
		logger.info("Checking custom configuration in environment variable");
		if(configPath==null) {
			logger.info("Environment variable not found, trying system property");
			configPath = System.getProperty(ENV_VERONICA_CONFIG);
		}
		if(configPath!=null) {
			logger.info("Loading custom configuration");
			Configuration overrideConfig = loadConfiguration(ConfigType.PROP, configPath);
			config = new CompositeConfiguration(Arrays.asList(configuration, overrideConfig));
		}else{
			config = configuration;
		}
	}
	
	private Configuration loadDefaultConfiguration() throws ConfigurationException, URISyntaxException {
		return loadConfiguration(ConfigType.PROP, ConfigurationManager.class.getResource("/config/veronica.properties").toURI().toASCIIString());
	}

	protected Configuration loadConfiguration(ConfigType type, String configPath) throws ConfigurationException {
		Configuration configuration = null;
		switch (type) {
		case PROP:configuration = new PropertiesConfiguration(configPath);
			break;
		case XML:configuration = new XMLConfiguration(configPath);
			break;
		default:throw new ConfigurationException("Invalid configuration type");
		}
		return configuration;
	}

	public static ConfigurationManager getInstance() throws ConfigurationException, URISyntaxException {
		if(self==null) {
			self = new ConfigurationManager();
		}
		return self;
	}
	
	public Configuration getSecurityConfiguration() {
		return config.subset(CONFIG_SECURITY_PREFIX);
	}
	
	public Configuration getStorageConfiguration() {
		return config.subset(CONFIG_STORAGE_PREFIX);
	}

	/**
	 * Get All configuration in the raw format
	 * @return global veronica configuration
	 */
	public Configuration getRawConfig() {
		return config;
	}

	/**
	 * Get All configuration without the base prefix
	 * @return global veronica configuration
	 */
	public Configuration getConfig() {
		return config.subset(CONFIG_BASE_PREFIX);
	}
	
}
