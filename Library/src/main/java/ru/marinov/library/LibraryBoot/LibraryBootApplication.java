package ru.marinov.library.LibraryBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.marinov.library.LibraryBoot.models.SearchRequest;

@SpringBootApplication
public class LibraryBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryBootApplication.class, args);
	}

	@Bean
	public SearchRequest searchRequest() {
		return new SearchRequest();
	}
}
