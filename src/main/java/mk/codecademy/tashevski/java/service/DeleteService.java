package mk.codecademy.tashevski.java.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.Day;
import mk.codecademy.tashevski.java.model.Meal;
import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.model.WORKOUT;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.DayRepo;
import mk.codecademy.tashevski.java.repository.MealRepo;
import mk.codecademy.tashevski.java.repository.PostRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
import mk.codecademy.tashevski.java.repository.WorkoutRepo;

@Service
@RequiredArgsConstructor
public class DeleteService {
	
	
	
	private final  WorkoutRepo workoutRepo;
	
	 
	private final MealRepo mealRepo;
	
	
	private final DayRepo dayRepo;
	
	
	private final PostRepo postRepo;
	
	
	private final WeightlifterRepo weightlifterRepo;

	
	public void deleteWorkout(Long id, String mainUser) {
		Optional<WORKOUT> workout =  workoutRepo.findById(id);
		
		if(workout.isEmpty()) {
			throw new NoSuchElementException();
		}
		
		String username = workout.get().getDay()
				.getMonthlySchedule().getWeightlifter().getUsername();
		
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		
		workoutRepo.deleteById(id);
	}

	public void deleteMeal(Long id,String mainUser) {
		Optional<Meal> meal = mealRepo.findById(id);
		if(meal.isEmpty()) {
			throw new NoSuchElementException();
		}
		String username = meal.get().getDay()
				.getMonthlySchedule().getWeightlifter().getUsername();
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		mealRepo.deleteById(id);
	}

	public void deleteSupplement(String dayId,String supplement,String mainUser) {
		boolean isSupplementPresentInSet;
		Optional<Day>  dayO =  dayRepo.getDayForSupplementDeletion(dayId);
		if(dayO.isEmpty()) {
			throw new NoSuchElementException("Day does not exist");
		}
			Day day = dayO.get();
			
		String username =day.getMonthlySchedule().getWeightlifter().getUsername();
		
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		
		isSupplementPresentInSet = day.getSupplements().remove(supplement);
		if(!isSupplementPresentInSet) {
			throw new NoSuchElementException("Supplement does not exist");
		}
		dayRepo.save(day);
	}

	public void deletePost(Long id,String mainUser) {
		Optional<Post> post = postRepo.findById(id);
		if(post.isEmpty()) {
			throw new NoSuchElementException();
		}
		String username = post.get().getWeightlifter().getUsername();
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		postRepo.deleteById(id);
		
	}


	public void deleteFriend(String username1, String username2,String mainUser) {
		if(mainUser.equals(username1)||mainUser.equals(username2)) {
		
		Optional<Weightlifter> weightlifterOp1 = weightlifterRepo.getWeightlifterWithFriends(username1);
		
		Optional<Weightlifter> weighlifterOp2 = weightlifterRepo.getWeightlifterWithFriends(username2);
		
		if(!(weightlifterOp1.isPresent() && weighlifterOp2.isPresent() )) {
			throw new NoSuchElementException();
		}
		
		Weightlifter weightlifter1 = weightlifterOp1.get();
		Weightlifter weightlifter2 = weighlifterOp2.get();
		
		weightlifter1.getMyFriends().remove(weightlifter2);
		weightlifter2.getMyFriends().remove(weightlifter1);
		
		weightlifterRepo.save(weightlifter1);
		weightlifterRepo.save(weightlifter2);
		
		}
		else {
			throw new IlegalAccessApiException();
		}
	}

}
