package mk.codecademy.tashevski.java.exceptions;

import lombok.Getter;

@Getter
public class FileNotImageException extends RuntimeException {
	
	private final String mainUsername;
	public FileNotImageException(String string,String mainUsername) {
		super(string);
		this.mainUsername = mainUsername;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8518469645682074136L;

}
