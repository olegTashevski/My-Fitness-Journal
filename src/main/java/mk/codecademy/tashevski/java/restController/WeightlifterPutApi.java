package mk.codecademy.tashevski.java.restController;

import java.sql.Date;
import java.time.LocalDate;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
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
	public String LastNameChanged(@RequestParam String username,@RequestParam String newLastName) {
		weightlifterPutService.changeLastName(username,newLastName);
		return "Last name has been changed";
	}
	
	@PutMapping("/changeGender")
	public String GenderChanged(@RequestParam String username,@RequestParam String newGender) {
		weightlifterPutService.changeGender(username,newGender);
		return "Gender has been changed";
	}
	
	@PutMapping("/changeDateOfBirth")
	public String DateOfBirthChanged(@RequestParam String username,@RequestParam Date newDateOfBirth) {
		weightlifterPutService.changeDateOfBirth(username,newDateOfBirth);
		return "Date of Birth has been changed";
	}
	
	@PutMapping("/updateBio")
	public String updateBio(@RequestParam String username,@RequestParam @Size(max = 80) String newBio) {
		weightlifterPutService.upadteBio(username,newBio);
		return "Successfully updated bio";
	}
	
	
	
	

}
