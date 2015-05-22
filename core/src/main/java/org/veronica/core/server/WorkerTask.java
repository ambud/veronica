package org.veronica.core.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WorkerTask implements Runnable {

	private SocketChannel channel;

	public WorkerTask(SocketChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		try {
			Socket socket = channel.socket();
			ObjectInputStream objects = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			
		}
	}

}
