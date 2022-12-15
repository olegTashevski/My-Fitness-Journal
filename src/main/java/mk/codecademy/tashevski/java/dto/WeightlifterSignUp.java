package mk.codecademy.tashevski.java.dto;

import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightlifterSignUp {
	
	@Size(min = 4 ,max=15,message="username has to be  from 4 to 15 characters")
	private String username;
	
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$",message = "Password isn't strong enough")
	private String password;
	
	
	@Size(min=1,max = 15,message = "first name must have from 1 to 15 letters")
	private String firstname;
	
	
	@Size(min=1,max = 20,message = "last name must have from 1 to 20 letters")
	private String lastName;
	
	
	@Pattern(regexp =  "(?:^|\\W)female(?:$|\\W)|(?:^|\\W)male(?:$|\\W)",message = "gender must be female or male")
	private String gender;
	
	@NotNull(message = "date of birth nust be entered")
	private Date dateOfBirth;
	

}
