package com.wait.app;

import com.tangzc.autotable.springboot.EnableAutoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 天
 */
@EnableAutoTable
@SpringBootApplication
@Slf4j
public class PetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetApiApplication.class, args);
	}

}
