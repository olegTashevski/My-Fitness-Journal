package mk.codecademy.tashevski.java.dto;


import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MealDto {
	
	@NotBlank
	private String usernameWeightlifter;
	@NotBlank
	private String idofDay;
	
	@NotBlank
	private String type;
	
	@NotBlank
	@Size(max = 50)
	private String nameOfMeal;
	
	
	private Set<@NotBlank @Size(max=25) String> ingredients;
	
	@NotBlank
	@Size(max=15)
	private String calories;
	
	@NotBlank
	@Size(max=15)
	private String protein;
	
	@NotBlank
	@Size(max=15)
	private String carbs;
	
	@NotBlank
	@Size(max=15)
	private String fats;

}
