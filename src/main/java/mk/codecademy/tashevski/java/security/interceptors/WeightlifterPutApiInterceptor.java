package mk.codecademy.tashevski.java.security.interceptors;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
@Component
@RequiredArgsConstructor
public class WeightlifterPutApiInterceptor implements HandlerInterceptor {
	
	private final  WeightlifterRepo weightlifterRepo;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String usernameParam = request.getParameter("username");
		System.out.println("usernameParam in PutApi Interceptor:"+usernameParam);
		String mainUser = (String)request.getAttribute("mainUser");
		System.out.println("mainUser in PutApi Interceptor:"+mainUser);
		if(!mainUser.equals(usernameParam)) {
			throw new IlegalAccessApiException();
		}
		
		if(request.getRequestURI().equals("/putApi/newRating")) {
			String friendUsername = request.getParameter("friendUsername");
			Optional<Weightlifter> isFriend = weightlifterRepo.findFriend(usernameParam, friendUsername);
			if(isFriend.isEmpty()) {
				throw new IlegalAccessApiException();
			}
		}
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
