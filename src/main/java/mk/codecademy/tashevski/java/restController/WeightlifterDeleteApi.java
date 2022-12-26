package mk.codecademy.tashevski.java.restController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static mk.codecademy.tashevski.java.Constants.MAIN_USER_ATT_NAME;
import mk.codecademy.tashevski.java.service.DeleteService;
@RequestMapping("/deleteApi/")
@RestController
public class WeightlifterDeleteApi {
	
	@Autowired
	private DeleteService deleteService;
	
	@DeleteMapping("/deleteWorkout")
	public String deleteWorkout(@RequestParam Long id,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		deleteService.deleteWorkout(id,mainUser);
		return "Successfully workout removed";
	}
	
	@DeleteMapping("/deleteMeal")
	public String deleteMeal(@RequestParam Long id,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		deleteService.deleteMeal(id,mainUser);
		return "Successfully deleted meal";
	}
	
	
	@DeleteMapping("/deleteSupplement")
	public String deleteSupplement(@RequestParam String dayId,@RequestParam String supplement,HttpServletRequest request) {
		
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		deleteService.deleteSupplement(dayId,supplement,mainUser);
		return "Successfully removed supplement";
	}
	
	@DeleteMapping("/deletePost")
	public String deletePost(@RequestParam Long id,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		deleteService.deletePost(id,mainUser);
		return "Successfully deleted post";
	}
	
	@DeleteMapping("/deleteFriend")
	public String deleteFriend(@RequestParam String username1 ,@RequestParam String username2,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		deleteService.deleteFriend(username1,username2,mainUser);
		return "Successfully deleted friend";
	}
	
	

}
