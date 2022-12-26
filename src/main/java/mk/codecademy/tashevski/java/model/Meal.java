package mk.codecademy.tashevski.java.model;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "day")
	private Day day;
	
	private String type;
	
	private String nameOfMeal;
	
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ingredients_of_meals",joinColumns = @JoinColumn(name="meal_id"))
	@Column(name = "ingredient")
	private Set<String> ingredients;
	
	private String calories;
	
	private String protein;
	
	private String carbs;
	
	private String fats;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Meal other = (Meal) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
