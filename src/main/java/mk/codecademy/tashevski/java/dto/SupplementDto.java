package mk.codecademy.tashevski.java.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Value;

@Value
public class SupplementDto {
	@NotBlank
	String usernameWeightlifter;
	@NotBlank
	private String idofDay;
	
	@NotBlank
	@Size(max = 20)
	private String supplement;

}
