package mk.codecademy.tashevski.java.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BothUsernames implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7889810216042898498L;
	private String fromUsername;
	private String toUsername;

}
