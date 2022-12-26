package mk.codecademy.tashevski.java.restController;



import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mk.codecademy.tashevski.java.dto.EditPost;
import mk.codecademy.tashevski.java.dto.MealDto;
import mk.codecademy.tashevski.java.dto.SupplementDto;
import mk.codecademy.tashevski.java.dto.WeightlifterSignUp;
import mk.codecademy.tashevski.java.dto.WorkoutDto;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.service.ExtrasService;
import mk.codecademy.tashevski.java.service.WeightlifterService;
import static mk.codecademy.tashevski.java.Constants.MAIN_USER_ATT_NAME;
@RestController
@RequestMapping("/postApi/")

public class WeigtlifterPostAPI {
	
	
	@Autowired
	ExtrasService extrasService;
	
	
	@Autowired
	private WeightlifterService weightlifterService; 
	
	
	
	@PostMapping("/addUser")
	public String addUser(@RequestBody WeightlifterSignUp weightlifter) {
		weightlifterService.addUser(weightlifter);
		return "Successfully added weightlifter";
	}
	
	
	@PostMapping("/addMouthlySchedule")
	public String addMouthlySchedule(@RequestParam(name = "us") String username,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		if(!mainUser.equals(username)) {
			throw  new IlegalAccessApiException();
		}
		weightlifterService.addMouthlySchedule(username);
		return "Successfully added mouthly schedule";
	}
	

	
	
	@PostMapping("/addMeal")
	String addMeal(@Valid @RequestBody MealDto mealDto,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		weightlifterService.addMeal(mealDto,mainUser);
		
		return "Meal successfully added";
	}
	
	
	@PostMapping("/addWorkout")
	String addWorkout(@Valid @RequestBody WorkoutDto workoutdto,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		weightlifterService.addWorkout(workoutdto,mainUser);
		
		return "Workout has been successfully addded";
	}
	
	
	@PostMapping("/addSupplement")
	String addSupplement(@Valid @RequestBody SupplementDto supplement,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		weightlifterService.addSupplement(supplement,mainUser);
		return "Successfully added suplement";
	}
	
	@PostMapping("/addProfilePic")
	String addProfileImage(@RequestParam String username, @RequestParam MultipartFile image,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		if(!mainUser.equals(username)) {
			throw new IlegalAccessApiException();
		}
		weightlifterService.addProfilePic(username,image);
		return "succesfully added profile picture";
	}
	
	
	@PostMapping("/editPost")
	public String editPost(@Valid  @ModelAttribute EditPost postEdit,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		weightlifterService.editPost(postEdit,mainUser);
		return "Successfully post edited";
	}
	

	


	
	
	
	
	
	
}
