package mk.codecademy.tashevski.java.exceptions;

import lombok.Getter;

@Getter
public class IlegalUsernameOrPassword extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5547939954879302196L;
	private final String reason;

	public IlegalUsernameOrPassword(String reason) {
		super(reason);
		this.reason = reason;
		
	}
	



}
