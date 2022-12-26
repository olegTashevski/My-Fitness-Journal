package mk.codecademy.tashevski.java.model;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Day {
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "monthlySchedule_id")
	private MonthlySchedule monthlySchedule;
	@Id
	@Column(length = 40)
	private String id;
	
	private LocalDate date;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "day",cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<Meal> meals;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "day",cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<WORKOUT> workouts;
	
	@ElementCollection
	@CollectionTable(name="supplements_taken_in_day",joinColumns = {@JoinColumn(name="day")})
	private Set<String> supplements;

		
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Day other = (Day) obj;
		return Objects.equals(this.id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	public Day(MonthlySchedule monthlySchedule, LocalDate date, Set<Meal> meals, Set<WORKOUT> workouts,
			Set<String> supplements) {
		super();
		this.monthlySchedule = monthlySchedule;
		this.date = date;
		this.meals = meals;
		this.workouts = workouts;
		this.supplements = supplements;
		this.id=date.toString() + monthlySchedule.getWeightlifter().getUsername();
	}
	
	public String getWeightlifterUsername() {
		
		return this.getMonthlySchedule().getWeightlifter().getUsername();
	}

	@Override
	public String toString() {
		return "Day [id=" + id + "]";
	}
	
	
	

}
