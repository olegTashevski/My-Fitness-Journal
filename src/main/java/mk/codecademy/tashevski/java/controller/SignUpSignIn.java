package mk.codecademy.tashevski.java.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mk.codecademy.tashevski.java.dto.WeightLifterSignIn;
import mk.codecademy.tashevski.java.dto.WeightlifterSignUp;
import mk.codecademy.tashevski.java.service.WeightlifterService;

@Controller
public class SignUpSignIn {
	
	private static final String REDIRECT_SIGN = "redirect:/";
	@Autowired
	private WeightlifterService weightlifterService;
	
	@GetMapping("")
	public ModelAndView sign(ModelAndView model) {
		
		try {
		if(model.getViewName()==null) {
			model = new ModelAndView("sign"); 
		}
		}
		catch (Exception e) {
			model = new ModelAndView("sign");
		}
		model.addObject("weightlifter", new WeightLifterSignIn());
		model.addObject("weightlifterSignUp", new WeightlifterSignUp());
		return model;
	}
	
	@GetMapping("/signedUp")
	public ModelAndView signedUp( @Valid @ModelAttribute WeightlifterSignUp weightlifter,RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("newWeightlifter", true);
		weightlifterService.addUser(weightlifter);
		return new ModelAndView(REDIRECT_SIGN);
	}
	
	@GetMapping("/logOut")
	public ModelAndView logOut(HttpServletResponse response) {
		
		Cookie deleteCookie = new Cookie("authentication", null);
		deleteCookie.setMaxAge(0);
		response.addCookie(deleteCookie);
		return new ModelAndView(REDIRECT_SIGN);
	}
	

}
