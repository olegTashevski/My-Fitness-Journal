package mk.codecademy.tashevski.java.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MonthlySchedule {
	
	@Id
	@Column(length = 30)
	private String id;
	
	private String month_year;
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "weightlifters")
	private Weightlifter weightlifter;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "monthlySchedule",cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<Day> days;

	public MonthlySchedule(String month_year_usernaem, Weightlifter weightlifter, Set<Day> days) {
		super();
		this.id=weightlifter.getUsername()+month_year_usernaem; 
		this.month_year = month_year_usernaem;
		this.weightlifter = weightlifter;
		this.days = days;
	}

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
		MonthlySchedule other = (MonthlySchedule) obj;
		return Objects.equals(id, other.id);
	}
	
	
	

}
 