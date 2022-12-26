package mk.codecademy.tashevski.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import mk.codecademy.tashevski.java.repository.MonthlyScheduleRepo;

@SpringBootApplication
@EnableScheduling
public class MyFinalProjectApplication {
	
	private MonthlyScheduleRepo monthlyScheduleRepo;

	public static void main(String[] args) {
		SpringApplication.run(MyFinalProjectApplication.class, args);
	}
	
	@Scheduled(cron = "0 0 0 1 * ?")
	public void deleteAllMonthlyShedulesAtTheBeginningOfEveryMonth() {
		monthlyScheduleRepo.deleteAll();
	}

}
