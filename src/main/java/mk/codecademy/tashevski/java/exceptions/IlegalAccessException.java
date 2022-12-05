package mk.codecademy.tashevski.java.exceptions;

import lombok.Getter;

@Getter
public class IlegalAccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4722181330456362149L;
	private String mainUsername;
	
	public IlegalAccessException(String mainUsername) {
		this.mainUsername = mainUsername;
	}

}
