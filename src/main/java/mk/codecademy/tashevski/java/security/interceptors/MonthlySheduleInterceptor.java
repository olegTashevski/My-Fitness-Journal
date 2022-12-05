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
import mk.codecademy.tashevski.java.exceptions.NotSignedInException;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;

@Component
public class MonthlySheduleInterceptor implements HandlerInterceptor {
	
	@Autowired
	private WeightlifterRepo weightlifterRepo;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("in interceptor monthly shedule");
		boolean throwExc = true;
		String type = null;
		String username = request.getParameter("username");
	
		try {
			for (Cookie cookie : request.getCookies())
			{
				
				if(cookie.getName().equals("authentication"))
				{	
					type=checkUsername(cookie.getValue(),username);
					throwExc=false;
				}
				
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(throwExc) {
			throw new NotSignedInException();
		}
		request.setAttribute("access", type);
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
			type="myPage";
		}
		else {
			Optional<Weightlifter> weightlifter = weightlifterRepo.findFriend(cookieUsername, paramUsername);
			if(weightlifter.isPresent()) {
				type="friend";
			}
			else {
				throw new IlegalAccessException(cookieUsername);
			}
		}
		return type;
	}

	
	
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	

}
