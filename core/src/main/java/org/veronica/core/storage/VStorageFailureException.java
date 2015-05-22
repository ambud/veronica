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
