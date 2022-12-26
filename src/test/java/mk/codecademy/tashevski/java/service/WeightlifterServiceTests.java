package mk.codecademy.tashevski.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mk.codecademy.tashevski.java.model.Day;
import mk.codecademy.tashevski.java.model.MonthlySchedule;
import mk.codecademy.tashevski.java.model.Weightlifter;
import mk.codecademy.tashevski.java.repository.WeightlifterRepo;
@ExtendWith(MockitoExtension.class)
class WeightlifterServiceTests {
	
	@Mock 
	private WeightlifterRepo weightlifterRepo;
	
	private WeightlifterService testService;

	@BeforeEach
	void setUp() throws Exception {
		testService = new WeightlifterService(null, weightlifterRepo, null, null, null, null, null, null);
		
	}

	@Test
	void testAddMouthlySheduleWhenUsernameIsValid() {
		final String username = "testUsername";
		LocalDate today = LocalDate.now();
		LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		int daysBetween =(int) ChronoUnit.DAYS.between(today, endOfMonth);
		String mouth_year = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		Weightlifter weightlifter = Weightlifter.builder()
				.username(username).build();
		
		when(weightlifterRepo.findById(username))
			.thenReturn(Optional.of(weightlifter));
		
		testService.addMouthlySchedule(username);
		
		final MonthlySchedule monthlySchedule = weightlifter.getMonthlySchedule();
		final Set<Day> days = monthlySchedule.getDays();
		assertThat(monthlySchedule).isNotNull();
		assertThat(monthlySchedule.getId()).isEqualTo(mouth_year+username);
		assertThat(days)
		.hasSizeLessThanOrEqualTo(daysBetween+1)
		.hasSizeGreaterThanOrEqualTo(daysBetween+1)
	//	.allMatch(day-> day.getId() == (day.getDate().toString()+username))
		.allMatch(day-> day.getDate().isAfter(today.minusDays(1)))
		.allMatch(day-> day.getDate().isBefore(endOfMonth.plusDays(1)))
		.allMatch(day-> day.getMeals()!=null)
		.allMatch(day-> day.getSupplements()!=null)
		.allMatch(day -> day.getWorkouts()!=null);
		
		verify(weightlifterRepo,times(1)).findById(username);
		verify(weightlifterRepo,times(1)).save(weightlifter);
		
	}
	

	@Test
	void testAddMouthlySheduleWhenUsernameIsInValid() {
		final String username = "testUsername";
		
		
		when(weightlifterRepo.findById(username))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> testService.addMouthlySchedule(username))
			.isInstanceOf(NoSuchElementException.class);
		
		
		
		verify(weightlifterRepo,times(1)).findById(username);
		verify(weightlifterRepo,never()).save(any());
		
	}

}
