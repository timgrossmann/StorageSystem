package exceptions;

import java.util.InputMismatchException;

public class NoNameForProductException extends InputMismatchException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6640618267771898513L;

	
	public NoNameForProductException() {
		super();
	}
	
	public NoNameForProductException(String text) {
		super(text);
	}

}
