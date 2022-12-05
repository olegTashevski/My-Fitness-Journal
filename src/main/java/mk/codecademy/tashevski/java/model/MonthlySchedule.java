package mk.codecademy.tashevski.java.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
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
	
	

}
 