package mk.codecademy.tashevski.java.service;

import java.util.function.BinaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.Day;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.DayRepo;
import mk.codecademy.tashevski.java.repository.MealRepo;
import mk.codecademy.tashevski.java.repository.PostRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
import mk.codecademy.tashevski.java.repository.WorkoutRepo;

@Service
public class DeleteService {
	
	@Autowired
	private WorkoutRepo workoutRepo;
	
	@Autowired 
	private MealRepo mealRepo;
	
	@Autowired
	private DayRepo dayRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private WeightlifterRepo weightlifterRepo;

	public void deleteWorkout(Long id, String mainUser) {
		String username = workoutRepo.findById(id).get().getDay()
				.getMonthlySchedule().getWeightlifter().getUsername();
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		workoutRepo.deleteById(id);
	}

	public void deleteMeal(Long id,String mainUser) {
		String username = mealRepo.findById(id).get().getDay()
				.getMonthlySchedule().getWeightlifter().getUsername();
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		mealRepo.deleteById(id);
	}

	public void deleteSupplement(String dayId,String supplement,String mainUser) {
		Day day =  dayRepo.getDayForSupplementDeletion(dayId);
		String username =day.getMonthlySchedule().getWeightlifter().getUsername();
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		day.getSupplements().remove(day.getSupplements().stream().filter(p->p.equals(supplement)).findAny().get());
		dayRepo.save(day);
	}

	public void deletePost(Long id,String mainUser) {
		String username = postRepo.findById(id).get().getWeightlifter().getUsername();
		if(!username.equals(mainUser)) {
			throw new IlegalAccessApiException();
		}
		postRepo.deleteById(id);
		
	}


	public void deleteFriend(String username1, String username2,String mainUser) {
		if(mainUser.equals(username1)||mainUser.equals(username2)) {
		
		Weightlifter weightlifter1 = weightlifterRepo.getWeightlifterWithFriends(username1);
		Weightlifter weighlifter2 = weightlifterRepo.getWeightlifterWithFriends(username2);
		weightlifter1.getMyFriends().remove(weighlifter2);
		weighlifter2.getMyFriends().remove(weightlifter1);
		weightlifterRepo.save(weightlifter1);
		weightlifterRepo.save(weighlifter2);
		}
		else {
			throw new IlegalAccessApiException();
		}
	}

}
