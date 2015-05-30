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
package org.veronicadb.core.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.veronicadb.core.configuration.ConfigurationManager;

public class VServer implements Callable<Void> {
	
	private static final Logger logger = Logger.getLogger("VServer");
	private static final String ALL_ADDRESSES = "0.0.0.0";
	private static final String PROP_BIND_HOST = "bind.address";
	private static final String PROP_BIND_PORT = "bind.port";
	private ServerSocketChannel channel;
	private final int port;

	public VServer() throws IOException, ConfigurationException {
		try {
			this.port = ConfigurationManager.getInstance().getConfig().getInt(PROP_BIND_PORT, 7992);
		} catch (ConfigurationException | URISyntaxException e) {
			throw new ConfigurationException(e);
		}
		this.channel = ServerSocketChannel.open();
	}
	
	@Override
	public Void call() throws Exception {
		ConfigurationManager configManager;
		try {
			configManager = ConfigurationManager.getInstance();
		} catch (ConfigurationException | URISyntaxException e1) {
			throw new Exception("Failed to get configuration manager instance");
		}
		try {
			this.channel = this.channel.bind(
					new InetSocketAddress(Inet4Address.getByName(configManager.getConfig().getString(PROP_BIND_HOST, ALL_ADDRESSES))
							, port), 5);
			logger.info("Started server at "+this.channel.getLocalAddress());
			this.channel.configureBlocking(false);
			while(true) {
				SocketChannel workerChannel = this.channel.accept();
				if(workerChannel!=null) {
					logger.info("New client connected:"+workerChannel.getRemoteAddress().toString());
					VChannelHandlers.getInstance().submitChannel(workerChannel);
				}
			}
		} catch (IOException e) {
			throw new Exception("Failed to bind server to bind address", e);
		} finally {
			this.channel.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			new VServer().call();
		} catch (Exception e) {
			System.err.println("Failure to launch Veronica server:"+e.getMessage());
		}
	}

}
