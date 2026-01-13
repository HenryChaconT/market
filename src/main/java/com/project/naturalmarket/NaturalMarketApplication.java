package com.project.naturalmarket;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;




@SpringBootApplication
public class NaturalMarketApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    

    public static void main(String[] args) {
        SpringApplication.run(NaturalMarketApplication.class, args);
    }

}
