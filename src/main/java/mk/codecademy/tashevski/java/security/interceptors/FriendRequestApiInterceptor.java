package mk.codecademy.tashevski.java.security.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessException;
@Component
public class FriendRequestApiInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String mainUser = (String)request.getAttribute("mainUser");
		String usernameParam = null;
		String fromUser = request.getParameter("fromUser");
		System.out.println("fromUser:"+fromUser);
		String username = request.getParameter("username");
		System.out.println("username:"+username);
		if(fromUser!= null || username!=null){
			if(fromUser!=null) {
				usernameParam = fromUser;
			}
			else {
				usernameParam = username;
			}
		}
		System.out.println("mainUser:"+mainUser);
		System.out.println("userParam"+usernameParam);
		if(!mainUser.equals(usernameParam)) {
			throw new  IlegalAccessApiException();
		}
		
		// TODO Auto-generated method stub
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	

}
