package com.urbanhop.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.urbanhop.demo.entity.TipoEntity;
import com.urbanhop.demo.repository.TipoRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataInitializer {
	

		@Autowired
	    private TipoRepository tipoRepository;

		@Bean
	    public CommandLineRunner loadData() {
	    	return args -> {
		        if (tipoRepository.count() == 0) {
		            tipoRepository.save(new TipoEntity(1, "administrador"));
		            tipoRepository.save(new TipoEntity(2, "cliente"));
		        }
	    	};
	    }  
	    	
}
