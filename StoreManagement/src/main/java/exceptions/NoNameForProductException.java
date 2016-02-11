package exceptions;

import java.util.InputMismatchException;

public class NoNameForProductException extends InputMismatchException {
	
	public NoNameForProductException() {
		super();
	}
	
	public NoNameForProductException(String text) {
		super(text);
	}

}
