package mk.codecademy.tashevski.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WeightLifterSignIn {
	private String username;
	private String password;
	
	

}
