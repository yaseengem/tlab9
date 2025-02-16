package com.tlab9.live;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TLab9 {

	public static void main(String[] args) {
		SpringApplication.run(TLab9.class, args);
	}

}
