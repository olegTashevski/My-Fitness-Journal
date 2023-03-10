package mk.codecademy.tashevski.java.advise;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import mk.codecademy.tashevski.java.controller.HomePage;
import mk.codecademy.tashevski.java.controller.OtherExceptionsController;
import mk.codecademy.tashevski.java.controller.SignUpSignIn;
import mk.codecademy.tashevski.java.exceptions.FileNotImageException;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessException;
import mk.codecademy.tashevski.java.exceptions.IlegalUsernameOrPassword;
import mk.codecademy.tashevski.java.exceptions.NotSignedInException;

@ControllerAdvice(basePackages = "mk.codecademy.tashevski.java.controller")
public class MVCExceptionHandler extends ExceptionHandlerExceptionResolver  {
	
	@Autowired
	private OtherExceptionsController otherExceptionsController;
	
	@Autowired
	private SignUpSignIn signController;
	
	@Autowired
	private HomePage homepageController;
	
	
	
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handelException(Exception ex,HttpServletRequest request) {
		
		if(ex instanceof IlegalUsernameOrPassword ilegalUsernameOrPassword) {
		return handelIlegalUsernameOrPassword(ilegalUsernameOrPassword);
		}
		if(ex instanceof FileNotImageException fileNotImageException ) {
			return handleFileNotImageException(fileNotImageException,request);
		}
		if(ex instanceof IlegalAccessException ilegalAccessException) {
			return handleIlegalAccessExeption(ilegalAccessException);
		}
		if(ex instanceof NotSignedInException notSignedInException) {
			return handleNotSignedInException(notSignedInException);
		}
		if(ex instanceof BindException bindException) {
			return handleBindException(bindException,request);
		}
		
		return otherExceptionsController.otherExceptionHandler(ex.getLocalizedMessage()) ;
	}
	
	
	
	
	
	
	


	
	protected ModelAndView handleBindException(BindException ex,HttpServletRequest request) {
		ModelAndView modelAndView;
		Map<String, String> errorsMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(er->errorsMap.put(er.getField(), er.getDefaultMessage()));
		boolean isRequestPost =  errorsMap.entrySet().stream().allMatch(er->
		er.getKey().equals("head")||er.getKey().equals("description"));
		String errors = errorsMap.entrySet().stream()
				.map(en->en.getKey()+":"+en.getValue()+"\n")
				.reduce("", (en1,en2)->(en1+en2));
		
		if(isRequestPost) {
			String username =(String) ex.getBindingResult().getFieldValue("weightlifterUsername");
			FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
			outputFlashMap.put("postErrors", errors);
			modelAndView = homepageController.myHomage(username);
			
		}
		else {
			modelAndView = new ModelAndView("sign");
			modelAndView.addObject("signUpErrors", errors);
			modelAndView = signController.sign(modelAndView);
		}
		return modelAndView; 
	}

	private ModelAndView handleNotSignedInException(NotSignedInException ex) {
		
		return signController.sign(null);
	}



	private ModelAndView handleIlegalAccessExeption(IlegalAccessException ex) {
		return homepageController.myHomage(ex.getMainUsername());
	}



	private ModelAndView handleFileNotImageException(FileNotImageException ex,HttpServletRequest request) {
		FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
		outputFlashMap.put("imageNotException", true);
		return homepageController.myHomage(ex.getMainUsername()); 
	}



	ModelAndView handelIlegalUsernameOrPassword(IlegalUsernameOrPassword ex) {
		String reason = ex.getReason();	
		ModelAndView modelAndView = new ModelAndView("sign");
		modelAndView.addObject("reason", reason);
		switch (reason) {
		case "Username doesn't exist" , "The password is incorrect" -> modelAndView.addObject("WrongSignInInfo", true);
		case "The username allready exists" -> modelAndView.addObject("userAllreadyExists", true);
		
		}
		
		return signController.sign(modelAndView);
	}

}
