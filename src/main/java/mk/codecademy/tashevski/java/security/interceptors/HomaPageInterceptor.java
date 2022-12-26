package mk.codecademy.tashevski.java.security.interceptors;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mk.codecademy.tashevski.java.exceptions.IlegalAccessException;
import mk.codecademy.tashevski.java.exceptions.IlegalUsernameOrPassword;
import mk.codecademy.tashevski.java.exceptions.NotSignedInException;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_ATT_NAME;
import static mk.codecademy.tashevski.java.Constants.AUTHEN_COOKIE_NAME;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_MYPAGE;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_FRIEND;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_NOTFRIEND;
import static mk.codecademy.tashevski.java.Constants.MAIN_USER_ATT_NAME;
@Component
public class HomaPageInterceptor implements HandlerInterceptor {
	private boolean myPage=false;
	@Autowired
	private WeightlifterRepo weightlifterRepo;
	
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String username = request.getParameter("username");
		
		if(request.getRequestURI().equals("/homepagaAfterPost")) {
			homepageAfterPost(request);
		}
		
		else if(request.getRequestURI().equals("/getHomepage")) {
			getHomepage(request, response, username);
		}
		else if(request.getRequestURI().equals("/homepage")) {
		String type = homepage(request, username);
		request.setAttribute(AUTHORI_ATT_NAME, type);
		} 
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	private String homepage(HttpServletRequest request, String username) {
		
			String type = null;
					
				if(myPage) {
					type = AUTHORI_TYPE_MYPAGE;
					myPage=false;
					return type;
				}
					
				boolean throwExc = true;
				final Cookie[] cookies = request.getCookies();
				if(cookies==null) {
					throw new NotSignedInException();
				}
				
				for (Cookie cookie : cookies){
					
					if(cookie.getName().equals(AUTHEN_COOKIE_NAME)) {	
						throwExc=false;
						type=checkUsername(cookie.getValue(),username);
					}
					
				}
				if(throwExc) {
					throw new NotSignedInException();
				}
			
			
			return type;
		
		
	}

	private void getHomepage(HttpServletRequest request, HttpServletResponse response, String username) {
	
			
			final Cookie[] cookies = request.getCookies();
			if(cookies==null) {
				authenticate(request, response, username);
				return;
			}
			
			
			String autheticatedUsername=null;
			
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(AUTHEN_COOKIE_NAME)) {
					autheticatedUsername = cookie.getValue();
					
				}
			}
			
			
			
			if(autheticatedUsername!=null && autheticatedUsername.equals(username)) {
				return;
			}
			else {
				Cookie deleteCookie = new Cookie(AUTHEN_COOKIE_NAME, null);
				deleteCookie.setMaxAge(0);
				response.addCookie(deleteCookie);
			}
			
			authenticate(request, response, username);
		
	}

	private void authenticate(HttpServletRequest request, HttpServletResponse response, String username) {
		Optional<Weightlifter> weightlifterO = weightlifterRepo.findById(username);
		
		if(weightlifterO.isEmpty()) {
			throw new IlegalUsernameOrPassword("Username doesn't exist");
		}
		Weightlifter weightlifter = weightlifterO.get();
		if(!request.getParameter("password").equals(weightlifter.getPassword())) {
			throw new IlegalUsernameOrPassword("The password is incorrect");
		}
		
		Cookie cookie = new Cookie(AUTHEN_COOKIE_NAME, username);
		cookie.setMaxAge(7200);
		response.addCookie(cookie);
		myPage=true;
	}

	private void homepageAfterPost(HttpServletRequest request) {
		
			boolean throwExc = true;
			
			final Cookie[] cookies = request.getCookies();
			if(cookies==null) {
				throw new NotSignedInException();
			}
			for (Cookie cookie : cookies) {
				
				if(cookie.getName().equals(AUTHEN_COOKIE_NAME)) {
					
					request.setAttribute(MAIN_USER_ATT_NAME, cookie.getValue());
					throwExc = false;
				}
			}
			
			if(throwExc) {
				throw new NotSignedInException();
			}
		
	}

	private  String checkUsername(String cookieUsername,String paramUsername) {
		String type=null;
		if(cookieUsername.equals(paramUsername)) {
			type=AUTHORI_TYPE_MYPAGE;
		}
		else if(weightlifterRepo.findFriend(cookieUsername, paramUsername).isPresent()){
			type=AUTHORI_TYPE_FRIEND;
		}
		else if(weightlifterRepo.findById(paramUsername).isPresent()) {
			type=AUTHORI_TYPE_NOTFRIEND;
			}
		else {
			throw new IlegalAccessException(cookieUsername);
		}
		
		return type;
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(!request.getRequestURI().equals("/homepage")) {
			return;
		}
		
		final boolean myPageBoolean = (boolean)modelAndView.getModel().get(AUTHORI_TYPE_MYPAGE);
		if(myPageBoolean) {
			return;
		}
		
		String mainUser = (String)(modelAndView.getModel().get(MAIN_USER_ATT_NAME));
		if(mainUser!=null && !mainUser.isBlank()) {
			return;
		}
		
		final Cookie[] cookies = request.getCookies();
		
		if(cookies==null) {
			return;
		}
		
		for (Cookie cookie : cookies)
		{
			
			if(cookie.getName().equals(AUTHEN_COOKIE_NAME))
			{	
				modelAndView.addObject(MAIN_USER_ATT_NAME, cookie.getValue());
			}
			
		}
		
	
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	

}
