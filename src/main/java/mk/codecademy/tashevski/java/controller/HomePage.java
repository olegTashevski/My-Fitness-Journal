package mk.codecademy.tashevski.java.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mk.codecademy.tashevski.java.dto.RequestPost;
import mk.codecademy.tashevski.java.dto.WeightLifterSignIn;
import mk.codecademy.tashevski.java.service.WeightLifterGetService;
import static  mk.codecademy.tashevski.java.Constants.REDIRECT_HOMEPAGE;

@Controller
public class HomePage {
	
	
	@Autowired
	private WeightLifterGetService weightlifterGetService;
	
	@GetMapping("/getHomepage")
	public String getHomePage(@ModelAttribute WeightLifterSignIn weightlifter ) {
		
		
		return REDIRECT_HOMEPAGE + weightlifter.getUsername();
	}
	
	@PostMapping("/homepagaAfterPost")
	public ModelAndView homepageAfterPost(@ModelAttribute @Valid RequestPost requestPost,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute("mainUser");
		weightlifterGetService.setModelForHomePageAfterPost(requestPost,mainUser);
		return new ModelAndView(REDIRECT_HOMEPAGE+requestPost.getWeightlifterUsername());
	}
	
	
	@GetMapping("/myHomaPage")
	public ModelAndView myHomage(@RequestParam String username) {
		
		return new ModelAndView(REDIRECT_HOMEPAGE+username);
	}
	
	@GetMapping("/homepage")
	public String homepage(@RequestParam String username,Model model,HttpServletRequest request) {
		String type = (String)request.getAttribute("authorization");
		weightlifterGetService.setModelForHomePage(username,model,type);
		return "homepage";
	}
	@GetMapping("/getHomapageForFriend")
	public ModelAndView homapageForFriend(@RequestParam String myUsername,@RequestParam String friendUsername,RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("mainUser",myUsername );
		return new ModelAndView(REDIRECT_HOMEPAGE+friendUsername);
	}
	
	@GetMapping("/getHomapageForSearchBar")
	public ModelAndView getHomapageForSearchBar(@RequestParam String myUsername , @RequestParam String otherUsername) {
		return 	new ModelAndView(REDIRECT_HOMEPAGE+otherUsername); 
		
	}
	
	
	

	

}
