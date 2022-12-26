package mk.codecademy.tashevski.java.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BothUsernames implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7889810216042898498L;
	private String fromUsername;
	private String toUsername;
	@Override
	public int hashCode() {
		return Objects.hash(fromUsername, toUsername);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BothUsernames other = (BothUsernames) obj;
		return Objects.equals(fromUsername, other.fromUsername) && Objects.equals(toUsername, other.toUsername);
	}

}
