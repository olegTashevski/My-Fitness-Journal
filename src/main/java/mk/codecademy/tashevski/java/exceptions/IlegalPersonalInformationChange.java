package mk.codecademy.tashevski.java.exceptions;

public class IlegalPersonalInformationChange extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1928353374672079798L;
	
	private String reason;
	
	public IlegalPersonalInformationChange(String reason) {
		super(reason);
		this.reason = reason;
	}
}
