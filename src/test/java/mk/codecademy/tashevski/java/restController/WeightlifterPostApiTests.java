package mk.codecademy.tashevski.java.restController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Set;

import javax.servlet.http.Cookie;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import mk.codecademy.tashevski.java.dto.MealDto;
import mk.codecademy.tashevski.java.model.Day;



import mk.codecademy.tashevski.java.repository.DayRepo;



import static mk.codecademy.tashevski.java.Constants.AUTHEN_COOKIE_NAME;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class WeightlifterPostApiTests {
	
	@Autowired
	private DayRepo repo;
	
	

	@Autowired
	private MockMvc mockMvc;


	@Test
	void testAddMeal() throws Exception {
		final String expectedTextResult = "Meal successfully added";
		final String username = "testUser";
		final String dayId = "testday";
		

		Day day = Day.builder()
				.id(dayId)
				.build();

		repo.save(day);
		final String calories = "testCalories";
		final String carbs = "testCarbs";
		final String fats = "testFats";
		final String ing = "testIngrident";
		final String nameOfMeal = "testMealName";
		final String protein = "proteinTest";
		final String type = "testType";
		MealDto mealDto = MealDto.builder()
				.calories(calories)
				.carbs(carbs)
				.fats(fats)
				.idofDay(dayId)
				.ingredients(Set.of(ing))
				.nameOfMeal(nameOfMeal)
				.protein(protein)
				.type(type)
				.usernameWeightlifter(username)
				.build();
		
		MvcResult result = mockMvc.perform(post("/postApi/addMeal")
				.cookie(new Cookie(AUTHEN_COOKIE_NAME, username))
				.content(asJsonString(mealDto))
				 .contentType(MediaType.APPLICATION_JSON)
			      )
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
		String textResult = result.getResponse().getContentAsString();
		assertThat(textResult).isEqualTo(expectedTextResult);
		day = repo.getDayWithMeals(dayId);
		assertTrue(day.getMeals()
				.stream()
				.anyMatch(meal->meal.getCalories().equals(calories)
						&&meal.getCarbs().equals(carbs)
						&&meal.getFats().equals(fats)
						&&meal.getIngredients().stream().allMatch(ingS->ingS.equals(ing))
						&&meal.getNameOfMeal().equals(nameOfMeal)
						&&meal.getProtein().equals(protein)
						&&meal.getType().equals(type)
						)
				);
		repo.deleteById(dayId);
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}  

}
