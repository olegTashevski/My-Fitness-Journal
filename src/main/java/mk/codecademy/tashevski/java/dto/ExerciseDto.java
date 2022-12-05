package mk.codecademy.tashevski.java.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Value;
@Value
public class ExerciseDto {
	
	@NotBlank(message ="Exercise must have a name")
	private String name;
	@Min(1)
	@Max(20)
	private int sets;
	private List<@Min(1)@Max(500)Integer> reps;

}
