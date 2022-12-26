package mk.codecademy.tashevski.java.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mk.codecademy.tashevski.java.security.interceptors.ApiInterceptor;
import mk.codecademy.tashevski.java.security.interceptors.FriendRequestApiInterceptor;
import mk.codecademy.tashevski.java.security.interceptors.HomaPageInterceptor;
import mk.codecademy.tashevski.java.security.interceptors.MonthlySheduleInterceptor;
import mk.codecademy.tashevski.java.security.interceptors.SignIntercetor;
import mk.codecademy.tashevski.java.security.interceptors.WeightlifterPutApiInterceptor;
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	private HomaPageInterceptor homaPageInterceptor;
	
	@Autowired 
	private MonthlySheduleInterceptor monthlySheduleInterceptor;
	
	@Autowired
	private ApiInterceptor apiInterceptor;
	
	@Autowired 
	private FriendRequestApiInterceptor friendRequestApiInterceptor;
	
	@Autowired
	private WeightlifterPutApiInterceptor putApiInterceptor;
	
	@Autowired
	private SignIntercetor signIntercetor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> homepage = new ArrayList<>(Arrays.asList(
				"/getHomepage","/homepage","/homepagaAfterPost"
				));
		List<String> apiPaths = new ArrayList<>(Arrays.asList(
				"/friendRequest/**","/getApi/**","/deleteApi/**"
				,"/putApi/**","/postApi/**"
				));
		registry.addInterceptor(homaPageInterceptor).addPathPatterns(homepage);
		registry.addInterceptor(monthlySheduleInterceptor).addPathPatterns("/getMonthlySheduleU");
		registry.addInterceptor(apiInterceptor).addPathPatterns(apiPaths);
		registry.addInterceptor(friendRequestApiInterceptor).addPathPatterns("/friendRequest/**").excludePathPatterns("/friendRequest/resolveRequest");
		registry.addInterceptor(putApiInterceptor).addPathPatterns("/putApi/**");
		registry.addInterceptor(signIntercetor).addPathPatterns("");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	

}
