package mk.codecademy.tashevski.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error/")
public class OtherExceptionsController {
	
	public ModelAndView otherExceptionHandler(String message) {	
		return  new ModelAndView("error","errorMessage",message);
	}

}
