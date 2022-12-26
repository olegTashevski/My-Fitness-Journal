package mk.codecademy.tashevski.java.security.interceptors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mk.codecademy.tashevski.java.exceptions.IlegalAccessException;
import mk.codecademy.tashevski.java.exceptions.NotSignedInException;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
import static mk.codecademy.tashevski.java.Constants.AUTHEN_COOKIE_NAME;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_ATT_NAME;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_MYPAGE;
import static mk.codecademy.tashevski.java.Constants.AUTHORI_TYPE_FRIEND;
@Component
public class MonthlySheduleInterceptor implements HandlerInterceptor {
	
	
	@Autowired
	private WeightlifterRepo weightlifterRepo;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean throwExc = true;
		String type = null;
		String username = request.getParameter("username");
		final Cookie[] cookies = request.getCookies();
		if(cookies==null) {
			throw new NotSignedInException();
		}
		for (Cookie cookie : cookies)
			{
				if(cookie.getName().equals(AUTHEN_COOKIE_NAME))
				{	
					type=checkUsername(cookie.getValue(),username);
					throwExc=false;
				}
				
			}
		 
		if(throwExc) {
			throw new NotSignedInException();
		}
		request.setAttribute(AUTHORI_ATT_NAME, type);
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	

	private String checkUsername(String cookieUsername, String paramUsername) {
		String type=null;
		if(cookieUsername.equals(paramUsername)) {
			type=AUTHORI_TYPE_MYPAGE;
		}
		else if (weightlifterRepo.findFriend(cookieUsername, paramUsername).isPresent()) {
			type=AUTHORI_TYPE_FRIEND;
		}
		else {
				throw new IlegalAccessException(cookieUsername);
		}
		
		return type;
	}

	
	
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	

}
