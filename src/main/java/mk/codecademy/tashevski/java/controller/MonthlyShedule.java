package mk.codecademy.tashevski.java.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mk.codecademy.tashevski.java.service.WeightLifterGetService;

@Controller
public class MonthlyShedule {
	
	@Autowired
	private WeightLifterGetService weightLifterGetService;
	
	
	
	@GetMapping("/getMonthlySheduleU")
	public String getMounthlyScheduleU(@RequestParam String username,Model model,HttpServletRequest request){
		String type = (String)request.getAttribute("access");
		weightLifterGetService.setMounthlyShedule(username, model,type);
		return "monthlyShedule";
	}
	

	
	
}
