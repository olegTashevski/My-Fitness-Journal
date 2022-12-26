package mk.codecademy.tashevski.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mk.codecademy.tashevski.java.exceptions.IlegalAccessApiException;
import mk.codecademy.tashevski.java.model.Day;
import mk.codecademy.tashevski.java.model.MonthlySchedule;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.DayRepo;
@ExtendWith(MockitoExtension.class)
class DeleteServiceTests {
	
	@Mock
	private DayRepo dayRepo;
	
	private  DeleteService deleteService;
	
	@BeforeEach
	void setUp() {
		deleteService = new DeleteService(null, null, dayRepo, null, null);
	}

	
	@Test
	void testDeleteSupplementWhenDayIdAndUsernameAreValidWillDeleteValidSupplement() {
		final String dayId = "testId";
		final String testSupplement = "testSupplement";
		final String username = "testUsername";
		Weightlifter testWeightlifter = Weightlifter.builder()
				.username(username)
				.build();
		MonthlySchedule testMonthlySchedule = MonthlySchedule.builder()
				.weightlifter(testWeightlifter)
				.build();
		Day day = Day.builder()
				.id(dayId)
				.monthlySchedule(testMonthlySchedule)
				.supplements(new HashSet<>(Arrays.asList(testSupplement)))
				.build();
		Optional<Day> optionalDay = Optional.of(day);
		when(dayRepo.getDayForSupplementDeletion(dayId))
			.thenReturn(optionalDay);
		 
		deleteService.deleteSupplement(dayId, testSupplement, username);
		
		verify(dayRepo,times(1))
			.getDayForSupplementDeletion(dayId);
		
		ArgumentCaptor<Day> dayArgumentCaptor =
				ArgumentCaptor.forClass(Day.class);
		
		verify(dayRepo,times(1))
			.save(dayArgumentCaptor.capture());
		
		assertThat(dayArgumentCaptor.getValue())
			.isEqualTo(day);
		
		assertThat(day.getSupplements())
			.doNotHave(
					new Condition<String>(sup->sup.equals(testSupplement) 
							,"The supplement set should not have"+testSupplement)
					);
		
	}
	
	@Test
	void testDeleteSupplementWhenDayIsNotPresent() {
		
		Optional<Day> optionalDay = Optional.empty();
		when(dayRepo.getDayForSupplementDeletion(any(String.class)))
			.thenReturn(optionalDay);
		final String anyValue = "testid";
		
		assertThatThrownBy(() -> deleteService.deleteSupplement(anyValue, null, null))
					.isInstanceOf(NoSuchElementException.class)
					.hasMessageContaining("Day does not exist");
		
		verify(dayRepo,never()).save(any());
		
	}
	
	@Test
	void testDeleteSupplementWhenMainUserIsNotEquealToUsername() {
		final String dayId = "testId";
		final String mainusername = "testUsername";
		final String otherTestUsername = "otherTestUsername";
		Weightlifter testWeightlifter = Weightlifter.builder()
				.username(otherTestUsername)
				.build();
		MonthlySchedule testMonthlySchedule = MonthlySchedule.builder()
				.weightlifter(testWeightlifter)
				.build();
		Day day = Day.builder()
				.id(dayId)
				.monthlySchedule(testMonthlySchedule)
				.build();
		
		Optional<Day> optionalDay = Optional.of(day);
		when(dayRepo.getDayForSupplementDeletion(dayId))
			.thenReturn(optionalDay);
		
		assertThatThrownBy(() -> deleteService.deleteSupplement(dayId, null, mainusername))
		.isInstanceOf(IlegalAccessApiException.class);
		verify(dayRepo,never()).save(any());
		
	}
	
	void testDeleteSupplementWhenSupplementIsNotValid() {
		
		final String dayId = "testId";
		final String testInSetSupplement = "testSupplement";
		final String testSupplementMethodArg = "otherTestSupplement";
		final String username = "testUsername";
		Weightlifter testWeightlifter = Weightlifter.builder()
				.username(username)
				.build();
		MonthlySchedule testMonthlySchedule = MonthlySchedule.builder()
				.weightlifter(testWeightlifter)
				.build();
		Day day = Day.builder()
				.id(dayId)
				.monthlySchedule(testMonthlySchedule)
				.supplements(new HashSet<>(Arrays.asList(testInSetSupplement)))
				.build();
		Optional<Day> optionalDay = Optional.of(day);
		when(dayRepo.getDayForSupplementDeletion(dayId))
			.thenReturn(optionalDay);
		 
		assertThatThrownBy(() -> deleteService.deleteSupplement(dayId, testSupplementMethodArg, username))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessageContaining("Supplement does not exist");
		
		verify(dayRepo,times(1))
			.getDayForSupplementDeletion(dayId);
		
				
		verify(dayRepo,never())
			.save(any());
		
	
		
	}
	
	

}
