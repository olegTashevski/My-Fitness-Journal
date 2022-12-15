package mk.codecademy.tashevski.java.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
	
	@Id
	private String username;
	
	private Long numberOfRatins;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="weightlifterRating",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private Set<SingleRating> singleRatings = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name="id")
	@MapsId
	private Weightlifter weightlifter;
	
	private float rating;
	

}
