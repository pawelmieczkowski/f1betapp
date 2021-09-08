package pl.salata.f1betapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class F1BetApp {

	public static void main(String[] args) {
		SpringApplication.run(F1BetApp.class, args);
	}

}
