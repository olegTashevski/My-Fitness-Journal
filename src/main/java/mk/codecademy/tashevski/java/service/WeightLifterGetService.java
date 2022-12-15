package mk.codecademy.tashevski.java.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mk.codecademy.tashevski.java.dto.GetPhoto;
import mk.codecademy.tashevski.java.dto.PostGet;
import mk.codecademy.tashevski.java.dto.RequestPost;
import mk.codecademy.tashevski.java.dto.WeightLifterSignIn;
import mk.codecademy.tashevski.java.dto.WeightlifterUsername;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessException;
import mk.codecademy.tashevski.java.exceptions.IlegalUsernameOrPassword;
import mk.codecademy.tashevski.java.model.Photo;
import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.model.SingleRating;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.MonthlyScheduleRepo;
import mk.codecademy.tashevski.java.repository.PhotoRepo;
import mk.codecademy.tashevski.java.repository.PostRepo;
import mk.codecademy.tashevski.java.repository.SingleRatingRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;

@Service
public class WeightLifterGetService {
	
	@Autowired
	private WeightlifterRepo weightlifterRepo;
	
	@Autowired
	private MonthlyScheduleRepo monthlyScheduleRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ExtrasService extrasService;
	
	@Autowired
	private PhotoRepo photoRepo;
	
	@Autowired
	private SingleRatingRepo singleRatingRepo; 

	

	public Slice<Post> getPosts(String username,String mainUser,int pageIndex) {
		if(!mainUser.equals(username)) {
			Optional<Weightlifter> weigtlifter = weightlifterRepo.findFriend(mainUser, username);
			if(weigtlifter.isEmpty()) {
				throw new IlegalAccessApiException();
			}
		}
		Pageable pageable = PageRequest.of(pageIndex,5,Sort.by(Direction.DESC, "timeStamp"));
		return postRepo.getPosts(username,pageable);
		}

	

	public Slice<String> getWeighliftersUsernamesForPattern(String pattern) {
		return  weightlifterRepo.selectUsernamesFromWeightlifters(pattern+"%",PageRequest.of(0, 4));
		
		 
	}

	public void setModelForHomePage(String weightlifter, Model model,String type) {
		model.addAttribute("weightlifterUsername",new WeightlifterUsername(weightlifter));
		model.addAttribute("weightlifter", weightlifterRepo.findById(weightlifter).get());
		
		if(type.equals("myPage")) {
			model.addAttribute("myPage", true);
			model.addAttribute("friend", false);
			model.addAttribute("notFriend", false);
		model.addAttribute("postRequest",RequestPost.builder().weightlifterUsername(weightlifter)
				.hasImages("true")
				.build());
		}
		else {
			model.addAttribute("myPage", false);
			if(type.equals("friend")) {
				model.addAttribute("friend", true);
				model.addAttribute("notFriend", false);
			}
			else {
				model.addAttribute("friend", false);
				model.addAttribute("notFriend", true);
			}
		}
		
	}

	public void setMounthlyShedule(String username, Model model,String type) {
		if(type.equals("myPage")) {
			model.addAttribute("myPage", true);
		}
		model.addAttribute("username", username);
		model.addAttribute("daysOfShedule", weightlifterRepo.findById(username).orElseThrow().getMonthlySchedule().getDays());
		
	}

	

	public ModelAndView homepageAfterSearchBar(ModelAndView model, String myUsername, String otherUsername,RedirectAttributes redirectAttributes) {
		if(myUsername.equals(otherUsername)) {
			model.setViewName("redirect:/homepage?username="+myUsername);
		}
		else {
			Optional<Weightlifter> otherWeightlifter = weightlifterRepo.findFriend(myUsername,otherUsername);
			if(otherWeightlifter.isPresent()) {
				model.setViewName("redirect:/getHomapageForFriend?myUsername="+myUsername+"&friendUsername="+otherUsername);
			}
			else {
				
				
				redirectAttributes.addFlashAttribute("mainUser",myUsername);
				model.setViewName("redirect:/homepage?username="+otherUsername);
			}
		}
		
		return model;
	}

	public PostGet getPost(Long id,String mainUser) {
		Post post = postRepo.getPostWithPhotos(id).orElseThrow();
		if(!mainUser.equals(post.getWeightlifter().getUsername())) {
			throw new IlegalAccessApiException();
		}
		Set<Long> images = post.getPhotos().stream()
				.map(ph-> ph.getId())
				.collect(Collectors.toSet());
		
		return new PostGet(post.getId(), post.getHead(), post.getDescription(), images) ;
	}

	public void setModelForHomePageAfterPost(RequestPost requestPost,String mainUser) {
		String username = requestPost.getWeightlifterUsername();
		if(!mainUser.equals(username)) {
			throw new IlegalAccessException(username);
		}
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		Post post = new Post(null, requestPost.getHead(), requestPost.getDescription(),Timestamp.valueOf(LocalDateTime.now()), null, weightlifter);
		Set<Post> posts = weightlifter.getPosts();
		posts.add(post);
		Set<Photo> imagesPost = new HashSet<>();
		System.out.println("-------------------------------");
		System.out.println(requestPost);
		if(requestPost.getHasImages().equalsIgnoreCase("true")) {
		for (MultipartFile imageTemp : requestPost.getImages()) {
			byte[] imageContent = extrasService.compress(imageTemp,username);
			Photo photo = new Photo(null, post, imageContent);
			photo=photoRepo.save(photo);
			imagesPost.add(photo);
		}
		post.setPhotos(imagesPost);
		}
		weightlifterRepo.save(weightlifter);
		
		
	}



	public String getRating(String username, String friend) {
		String response = "noRating";
		Optional<SingleRating> ratingOp = singleRatingRepo.getSingleRatingFromMainUserToFriend(username, friend);
		if(ratingOp.isPresent()) {
			response =""+ ratingOp.get().getRating();
		}
		
		return response;
	}

	

	
	

	

}
