package com.self.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.self.finance.model")
public class PersonalFinanceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalFinanceManagerApplication.class, args);
    }

}
