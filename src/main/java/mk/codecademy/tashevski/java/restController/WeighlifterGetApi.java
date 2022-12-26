package mk.codecademy.tashevski.java.restController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.dto.PostGet;
import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.service.ExtrasService;
import mk.codecademy.tashevski.java.service.WeightLifterGetService;
import static mk.codecademy.tashevski.java.Constants.MAIN_USER_ATT_NAME;

@RestController
@RequestMapping("/getApi/")
@RequiredArgsConstructor
public class WeighlifterGetApi {
	
	
	private final ExtrasService extrasService;
	
	
	private final WeightLifterGetService weightLifterGetService;
	
	@GetMapping("/ProfileImage")
	public byte[] getProfileImage(@RequestParam String weightlifterId,HttpServletResponse response) {
		response.setContentType("image/png, image/jpeg, image/pipeg, image/x-icon, image/jpg, image/png, image/gif");
		return extrasService.getProfileImage(weightlifterId);
	}
	
	@GetMapping("/getPosts")
	public Slice<Post> getPosts(@RequestParam String username,@RequestParam int pageIndex,HttpServletRequest request){
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		return weightLifterGetService.getPosts(username,mainUser,pageIndex);
	}
	
	@GetMapping("/getPostImage")
	public byte[] getPostImage(@RequestParam Long photoId,HttpServletResponse response,HttpServletRequest request) {
		response.setContentType("image/png, image/jpeg, image/pipeg, image/x-icon, image/jpg, image/png, image/gif");
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		return extrasService.getPostImage(photoId,mainUser);
	} 
	
	@GetMapping("/getWeighliftersUsernamesForPattern")
	 public Slice<String> getWeighliftersUsernamesForPattern(@RequestParam String pattern){
		return weightLifterGetService.getWeighliftersUsernamesForPattern(pattern);
	}
	
	@GetMapping("/getPost")
	public PostGet getPost(@RequestParam Long id,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute(MAIN_USER_ATT_NAME);
		return weightLifterGetService.getPost(id,mainUser);
	}
	
	@GetMapping("/getRating")
	public String getRating(@RequestParam String username,@RequestParam String friend) {
		return weightLifterGetService.getRating(username,friend);
	}

}
