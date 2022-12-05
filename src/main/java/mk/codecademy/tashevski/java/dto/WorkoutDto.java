package mk.codecademy.tashevski.java.dto;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Value;

@Value
public class WorkoutDto {
	@NotBlank
	String usernameWeightlifter;
	@NotBlank
	private String idofDay;
	
	Set<@Valid ExerciseDto> exercises;

}
