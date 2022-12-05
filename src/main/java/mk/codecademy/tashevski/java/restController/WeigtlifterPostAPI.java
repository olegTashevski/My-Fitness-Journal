package mk.codecademy.tashevski.java.restController;



import java.io.InputStream;
import java.util.Base64;
import java.util.Iterator;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.executable.ValidateOnExecution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;
import mk.codecademy.tashevski.java.dto.*;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.Photo;
import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.repository.PostRepo;
import mk.codecademy.tashevski.java.service.ExtrasService;
import mk.codecademy.tashevski.java.service.WeightlifterService;

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
		String mainUser = (String) request.getAttribute("mainUser");
		if(!mainUser.equals(username)) {
			throw  new IlegalAccessApiException();
		}
		weightlifterService.addMouthlySchedule(username);
		return "Successfully added mouthly schedule";
	}
	

	
	
	@PostMapping("/addMeal")
	String addMeal(@Valid @RequestBody MealDto mealDto,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute("mainUser");
		System.out.println("Username:"+mealDto.getUsernameWeightlifter());
		weightlifterService.addMeal(mealDto,mainUser);
		
		return "Meal successfully added";
	}
	
	
	@PostMapping("/addWorkout")
	String addWorkout(@Valid @RequestBody WorkoutDto Workoutdto,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute("mainUser");
		weightlifterService.addWorkout(Workoutdto,mainUser);
		
		return "Workout has been successfully addded";
	}
	
	
	@PostMapping("/addSupplement")
	String addSupplement(@Valid @RequestBody SupplementDto supplement,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute("mainUser");
		weightlifterService.addSupplement(supplement,mainUser);
		return "Successfully added suplement";
	}
	
	@PostMapping("/addProfilePic")
	String addProfileImage(@RequestParam String username, @RequestParam MultipartFile image,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute("mainUser");
		if(!mainUser.equals(username)) {
			throw new IlegalAccessApiException();
		}
		weightlifterService.addProfilePic(username,image);
		return "succesfully added profile picture";
	}
	
	
	@PostMapping("/editPost")
	public String editPost(@ModelAttribute @Valid EditPost postEdit,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute("mainUser");
		weightlifterService.editPost(postEdit,mainUser);
		return "Successfully post edited";
	}
	

	


	
	
	
	
	
	
}
