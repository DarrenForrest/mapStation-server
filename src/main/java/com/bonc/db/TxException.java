package com.bonc.db;

public class TxException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TxException() {
		
	}

	public TxException(String message) {
		super(message);
		
	}

	public TxException(Throwable cause) {
		super(cause);
		
	}

	public TxException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
