package mk.codecademy.tashevski.java.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WORKOUT {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE )
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="day")
	private Day day;
	
	
	@JsonManagedReference
	@OneToMany(mappedBy = "workout",cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<Exercise> exercises;


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WORKOUT other = (WORKOUT) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}
