package mk.codecademy.tashevski.java.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mk.codecademy.tashevski.java.dto.EditPost;
import mk.codecademy.tashevski.java.dto.ExerciseDto;
import mk.codecademy.tashevski.java.dto.MealDto;
import mk.codecademy.tashevski.java.dto.SupplementDto;
import mk.codecademy.tashevski.java.dto.WeightlifterSignUp;
import mk.codecademy.tashevski.java.dto.WorkoutDto;
import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.exceptions.IlegalUsernameOrPassword;
import mk.codecademy.tashevski.java.model.Day;
import mk.codecademy.tashevski.java.model.Exercise;
import mk.codecademy.tashevski.java.model.Meal;
import mk.codecademy.tashevski.java.model.MonthlySchedule;
import mk.codecademy.tashevski.java.model.Photo;
import mk.codecademy.tashevski.java.model.Post;
import mk.codecademy.tashevski.java.model.WORKOUT;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.DayRepo;
import mk.codecademy.tashevski.java.repository.ExerciseRepo;
import mk.codecademy.tashevski.java.repository.MealRepo;
import mk.codecademy.tashevski.java.repository.PhotoRepo;
import mk.codecademy.tashevski.java.repository.PostRepo;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
import mk.codecademy.tashevski.java.repository.WorkoutRepo;

@Service
@RequiredArgsConstructor()
public class WeightlifterService {
	
	
	
	private final ExtrasService extrasService;
	
	private final WeightlifterRepo weightlifterRepo;
	
	
	private final MealRepo mealRepo;
	

	private final  ExerciseRepo exerciseRepo;
	

	private final WorkoutRepo workoutRepo;
	
	
	private final PhotoRepo photoRepo;
	
	
	private final PostRepo postRepo;
	
	
	private final DayRepo dayRepo;
	
	
	
	
	
	public void addUser( WeightlifterSignUp weightlifterDto) {
		for (Weightlifter lifter : weightlifterRepo.findAll()) {
			if(lifter.getUsername().equals(weightlifterDto.getUsername().trim())) {
				throw new IlegalUsernameOrPassword("The username allready exists");
			}
		}
		Weightlifter weightlifter = new Weightlifter(weightlifterDto.getUsername()
				,weightlifterDto.getPassword() , weightlifterDto.getFirstname()
				, weightlifterDto.getLastName(), weightlifterDto.getGender()
				, weightlifterDto.getDateOfBirth(), "", null, null,null, new HashSet<>(), new HashSet<>(), new HashSet<>()); 
		weightlifterRepo.save(weightlifter);
	}

	public void addMouthlySchedule(String username) {
		MonthlySchedule monthlySchedule = new MonthlySchedule();
		LocalDate today = LocalDate.now();
		String mouth_year = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		monthlySchedule.setMonth_year(mouth_year);
		LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		long daysBetween = ChronoUnit.DAYS.between(today, endOfMonth);
		Set<Day> days = new HashSet<>();
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		for(int i=0;i<daysBetween+1;i++) {
			Day day = new Day();
			day.setMeals(new HashSet<>());
			day.setWorkouts(new HashSet<>());
			day.setSupplements(new HashSet<>());
			day.setDate(today.plusDays(i));
			days.add(day);
			day.setId(day.getDate().toString()+weightlifter.getUsername());
			day.setMonthlySchedule(monthlySchedule);
		}
		
		monthlySchedule.setDays(days);
		
		monthlySchedule.setWeightlifter(weightlifter);
		monthlySchedule.setId(mouth_year+weightlifter.getUsername());
		weightlifter.setMonthlySchedule(monthlySchedule);
		weightlifterRepo.save(weightlifter);
		
	}

	

	

	public void addMeal(MealDto mealDto,String mainUser) {
		String username = mealDto.getUsernameWeightlifter();
		if(!mainUser.equals(username)) {
			throw new IlegalAccessApiException();
		}
		
				Day day = dayRepo.getDayWithMeals(mealDto.getIdofDay()); 
				Set<Meal> meals = day.getMeals();
				Meal meal = new Meal(null, day, mealDto.getType(), mealDto.getNameOfMeal(), mealDto.getIngredients(), mealDto.getCalories(), mealDto.getProtein(), mealDto.getCarbs(), mealDto.getFats());
				meal = mealRepo.save(meal);
				meals.add(meal);
				dayRepo.save(day);
			
		
		
	}
	
	public void addWorkout(WorkoutDto workoutDto, String mainUser) {
		String username = workoutDto.getUsernameWeightlifter();
		if(!mainUser.equals(username)) {
			throw new IlegalAccessApiException();
		}
		
		
				Day day = dayRepo.getDayWithWorkouts(workoutDto.getIdofDay());
				Set<WORKOUT> workouts = day.getWorkouts();
				Set<Exercise> exercises = new HashSet<>();
				WORKOUT workout = new WORKOUT();
				workout.setDay(day);
				workout = workoutRepo.save(workout);
				for (ExerciseDto exerciseDto : workoutDto.getExercises()) {
					Exercise exercise = new Exercise(null, workout, exerciseDto.getName(),exerciseDto.getSets(), exerciseDto.getReps());
					exercise = exerciseRepo.save(exercise);
					exercises.add(exercise);
				}
				workout.setExercises(exercises);
				workouts.add(workout);
				dayRepo.save(day);
	}

	public void addSupplement(SupplementDto supplementDto, String mainUser) {
		String username = supplementDto.getUsernameWeightlifter();
		if(!mainUser.equals(username)) {
			throw new IlegalAccessApiException();
		}
				Optional<Day> dayO = dayRepo.getDayForSupplementDeletion(supplementDto.getIdofDay());
				
				if(dayO.isEmpty()) {
					throw new NoSuchElementException();
				}
				
				Day day = dayO.get();
				
				Set<String>	supplements = day.getSupplements();
				
				supplements.add(supplementDto.getSupplement());
				dayRepo.save(day);
	}

	public void addProfilePic(String username,MultipartFile image) {
		byte[] proficePic= extrasService.compress(image,username);
		Weightlifter weightlifter = weightlifterRepo.findById(username).orElseThrow();
		weightlifter.setProfilePic(proficePic);
		weightlifterRepo.save(weightlifter);
		
		
	}

	public void editPost(EditPost postEdit, String mainUser) {
		Post post = postRepo.getPostWithPhotos(postEdit.getPostId()).orElseThrow();
		String username = post.getWeightlifter().getUsername();
		if(!mainUser.equals(username)) {
			throw new IlegalAccessApiException();
		}
		Set<Photo> images = post.getPhotos();
		if(postEdit.getNewImages()!=null) {
		for (MultipartFile image : postEdit.getNewImages()) {
			images.add(new Photo(null, post, extrasService.compress(image,mainUser)));
		}
	}
		if(postEdit.getImagesIdsRemoved()!=null) {
		for(Long id : postEdit.getImagesIdsRemoved()) {
		 Photo photo = images.stream().filter(ph->ph.getId().equals(id))
				 .findFirst()
				 .orElseThrow();
		 images.remove(photo);
		 photoRepo.deleteById(id);
		 
		}
	}
		post.setHead(postEdit.getNewHead());
		post.setDescription(postEdit.getNewDescription());
		postRepo.save(post);
		
	}
	
	
	
	
	
	
	

}
