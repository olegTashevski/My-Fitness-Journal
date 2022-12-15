package mk.codecademy.tashevski.java.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import mk.codecademy.tashevski.java.dto.RequestPost;
import mk.codecademy.tashevski.java.dto.WeightLifterSignIn;
import mk.codecademy.tashevski.java.dto.WeightlifterUsername;
import mk.codecademy.tashevski.java.service.WeightLifterGetService;

@Controller
public class HomePage {
	
	@Autowired
	private WeightLifterGetService weightlifterGetService;
	
	@GetMapping("/getHomepage")
	public String getHomePage(@ModelAttribute WeightLifterSignIn weightlifter ) {
		
		return "redirect:/homepage?username=" + weightlifter.getUsername();
	}
	
	@PostMapping("/homepagaAfterPost")
	public ModelAndView homepageAfterPost(@ModelAttribute @Valid RequestPost requestPost,HttpServletRequest request) {
		String mainUser = (String) request.getAttribute("mainUser");
		weightlifterGetService.setModelForHomePageAfterPost(requestPost,mainUser);
		return new ModelAndView("redirect:/myHomaPage?username="+requestPost.getWeightlifterUsername());
	}
	
	
	@GetMapping("/myHomaPage")
	public ModelAndView myHomage(@RequestParam String username) {
		
		return new ModelAndView("redirect:/homepage?username="+username);
	}
	
	@GetMapping("/homepage")
	public String homepage(@RequestParam String username,Model model,HttpServletRequest request) {
		String type = (String)request.getAttribute("authentication");
		weightlifterGetService.setModelForHomePage(username,model,type);
		return "homepage";
	}
	@GetMapping("/getHomapageForFriend")
	public ModelAndView homapageForFriend(@RequestParam String myUsername,@RequestParam String friendUsername,RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("mainUser",myUsername );
		return new ModelAndView("redirect:/homepage?username="+friendUsername);
	}
	
	@GetMapping("/getHomapageForSearchBar")
	public ModelAndView getHomapageForSearchBar(@RequestParam String myUsername , @RequestParam String otherUsername,RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView =  new ModelAndView(); 
		return weightlifterGetService.homepageAfterSearchBar(modelAndView,myUsername,otherUsername,redirectAttributes);
		
	}
	
	
	

	

}
