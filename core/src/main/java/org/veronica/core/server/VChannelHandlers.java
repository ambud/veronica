package org.veronica.core.server;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VChannelHandlers {

	private static VChannelHandlers self = new VChannelHandlers();
	private ExecutorService es;
	
	private VChannelHandlers() {
		es = Executors.newFixedThreadPool(10);
	}
	
	public static VChannelHandlers getInstance() {
		return self;
	}
	
	public void submitChannel(SocketChannel channel) {
		es.submit(new WorkerTask(channel));
	}
	
}
