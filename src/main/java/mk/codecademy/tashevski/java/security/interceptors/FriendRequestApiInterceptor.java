package mk.codecademy.tashevski.java.security.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import static mk.codecademy.tashevski.java.Constants.MAIN_USER_ATT_NAME;
@Component
public class FriendRequestApiInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String mainUser = (String)request.getAttribute(MAIN_USER_ATT_NAME);
		
		String usernameParam = "";
		
		String fromUser = request.getParameter("fromUser");
		
		String username = request.getParameter("username");
		
	
			if(fromUser!=null) {
				usernameParam = fromUser;
			}
			else if(username!=null){
				usernameParam = username;
			}
		
		
		if(!mainUser.equals(usernameParam)) {
			throw new  IlegalAccessApiException();
		}
		
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	

}
