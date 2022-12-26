package mk.codecademy.tashevski.java.security.interceptors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import static mk.codecademy.tashevski.java.Constants.AUTHEN_COOKIE_NAME;
@Component
public class SignIntercetor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		final Cookie[] cookies = request.getCookies();
		if(cookies==null) {
			return true;
		}
		
		for (Cookie cookie : cookies)
		{
			
			if(cookie.getName().equals(AUTHEN_COOKIE_NAME))
			{
				
				response.sendRedirect("/homepage?username="+cookie.getValue());
			}
			
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
