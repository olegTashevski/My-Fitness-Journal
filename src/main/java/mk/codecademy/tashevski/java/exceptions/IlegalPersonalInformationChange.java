package mk.codecademy.tashevski.java.exceptions;

import lombok.Getter;

@Getter
public class IlegalPersonalInformationChange extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1928353374672079798L;
	
	private final String reason;
	
	public IlegalPersonalInformationChange(String reason) {
		super(reason);
		this.reason = reason;
	}
}
