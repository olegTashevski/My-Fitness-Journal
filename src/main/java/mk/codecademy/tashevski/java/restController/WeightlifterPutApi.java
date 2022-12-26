package mk.codecademy.tashevski.java.restController;

import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mk.codecademy.tashevski.java.service.WeightlifterPutService;

@Validated
@RestController
@RequestMapping("/putApi/")
public class WeightlifterPutApi {
	
	@Autowired
	private WeightlifterPutService weightlifterPutService;
	
	
	@PutMapping("/changePassword")
	public String passwordChanged(@RequestParam String username,@RequestParam String newPassword) {
		weightlifterPutService.changePassword(username,newPassword);
		return "Password has been changed";
	}
	
	@PutMapping("/changeFirstName")
	public String firstNameChanged(@RequestParam String username,@RequestParam String newFirstName) {
		weightlifterPutService.changeFirstName(username,newFirstName);
		return "First name has been changed";
	}
	
	@PutMapping("/changeLastName")
	public String lastNameChanged(@RequestParam String username,@RequestParam String newLastName) {
		weightlifterPutService.changeLastName(username,newLastName);
		return "Last name has been changed";
	}
	
	@PutMapping("/changeGender")
	public String genderChanged(@RequestParam String username,@RequestParam String newGender) {
		weightlifterPutService.changeGender(username,newGender);
		return "Gender has been changed";
	}
	
	@PutMapping("/changeDateOfBirth")
	public String dateOfBirthChanged(@RequestParam String username,@RequestParam Date newDateOfBirth) {
		weightlifterPutService.changeDateOfBirth(username,newDateOfBirth);
		return "Date of Birth has been changed";
	}
	
	@PutMapping("/updateBio")
	public String updateBio(@RequestParam String username,@RequestParam @Size(max = 80) String newBio) {
		weightlifterPutService.upadteBio(username,newBio);
		return "Successfully updated bio";
	}
	
	@PutMapping("/newRating")
	public String newRating(@RequestParam String username
			,@RequestParam String friendUsername
			,@RequestParam @Min(1) @Max(5) int rating ) {
		return "" + weightlifterPutService.giveRating(username,friendUsername,rating);
	}
	
	
	
	
	
	

}
