package mk.codecademy.tashevski.java.security.interceptors;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.thymeleaf.expression.Strings;

import antlr.StringUtils;
import mk.codecademy.tashevski.java.exceptions.IlegalUsernameOrPassword;
import mk.codecademy.tashevski.java.exceptions.NotSignedInException;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;

@Component
public class HomaPageInterceptor implements HandlerInterceptor {
	private boolean myPage;
	@Autowired
	private WeightlifterRepo weightlifterRepo;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("topka");
		String username = request.getParameter("username");
		if(request.getRequestURI().equals("/homepagaAfterPost")) {
			try {
				boolean throwExc = true;
				for (Cookie cookie : request.getCookies()) {
					System.out.println("cookie value:"+cookie.getValue());
					if(cookie.getName().equals("authentication")) {
						request.setAttribute("mainUser", cookie.getValue());
						throwExc = false;
					}
				}
				if(throwExc) {
					throw new Exception();
				}
				
			} catch (Exception e) {
				throw new NotSignedInException();
			}
			System.out.println("end of after post");
		}
		if(request.getRequestURI().equals("/getHomepage")) {
			try {
				boolean throwExc = true;
				for (Cookie cookie : request.getCookies()) {
					
					if(cookie.getName().equals("authentication")) {
						if(cookie.getValue()==username) {
							throwExc=false;
						}
						else {
							Cookie deleteCookie = new Cookie("authentication", null);
							deleteCookie.setMaxAge(0);
							response.addCookie(deleteCookie);
						}
					}
				}
				if(throwExc) {
					throw new Exception();
				}
				}
			catch (Exception e) {
				
			
			Optional<Weightlifter> weightlifterO = weightlifterRepo.findById(username); 
			if(weightlifterO.isEmpty()) {
				throw new IlegalUsernameOrPassword("Username doesn't exist");
			}
			Weightlifter weightlifter = weightlifterO.get();
			if(!request.getParameter("password").equals(weightlifter.getPassword())) {
				throw new IlegalUsernameOrPassword("The password is incorrect");
			}
			System.out.println("HomaPageInterceptor.afterCompletion()");
			System.out.println(username);
			Cookie cookie = new Cookie("authentication", username);
			cookie.setMaxAge(7200);
			response.addCookie(cookie);
			myPage=true;
			}
		}
		if(request.getRequestURI().equals("/homepage")) {
			try 
			{
				String type = null;
				System.out.println("In Interceptor for homapage "+request.getParameter("myPage"));
				try {
					if(myPage) {
						type = "myPage";
						myPage=false;
					}
					else {
						throw new Exception();
					}
				} catch (Exception  e) 
				{
					boolean throwExc = true;
					for (Cookie cookie : request.getCookies())
					{
						
						if(cookie.getName().equals("authentication"))
						{	
							throwExc=false;
							type=checkUsername(cookie.getValue(),username);
						}
						
					}
					if(throwExc) {
						throw new Exception();
					}
				}
				
				request.setAttribute("authentication", type);
			}
			catch (Exception e) {
				System.out.println(e);
				throw  new NotSignedInException();
			}
			
		} 
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	private  String checkUsername(String cookieUsername,String paramUsername) {
		String type=null;
		if(cookieUsername.equals(paramUsername)) {
			type="myPage";
		}
		else {
			Optional<Weightlifter> weightlifter = weightlifterRepo.findFriend(cookieUsername, paramUsername);
			if(weightlifter.isPresent()) {
				type="friend";
			}
			else {
				type="notFriend";
			}
		}
		return type;
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(!((boolean)modelAndView.getModel().get("myPage")==false)) {
			return;
		}
		String mainUser = (String)(modelAndView.getModel().get("mainUser"));
		if(mainUser!=null && !mainUser.isBlank()) {
			return;
		}
		for (Cookie cookie : request.getCookies())
		{
			
			if(cookie.getName().equals("authentication"))
			{	
				modelAndView.addObject("mainUser", cookie.getValue());
			}
			
		}
		
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	

}
