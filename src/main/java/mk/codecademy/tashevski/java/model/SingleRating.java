package mk.codecademy.tashevski.java.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SingleRating {
	
	
	
	
	@EmbeddedId
	private BothUsernames bothUsernames;
	
	private int rating;
	
	@ManyToOne
	@JoinColumn(name  ="weightlifterRating")
	private Rating weightlifterRating;
	
	
	
	
	

}
