package org.veronicadb.core.structures;

import java.io.IOException;

import org.veronicadb.core.VException;

public class ShardInitializationException extends VException {

	public ShardInitializationException(IOException e) {
		super(e);
	}

	private static final long serialVersionUID = 1L;

}
