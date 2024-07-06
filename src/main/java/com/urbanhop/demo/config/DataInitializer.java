package com.urbanhop.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.urbanhop.demo.entity.TallaEntity;
import com.urbanhop.demo.entity.TipoEntity;
import com.urbanhop.demo.repository.TallaRepository;
import com.urbanhop.demo.repository.TipoRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataInitializer {
	

		@Autowired
	    private TipoRepository tipoRepository;

		@Autowired
		private TallaRepository tallaRepository;
		@Bean
	    public CommandLineRunner loadData() {
	    	return args -> {
		        if (tipoRepository.count() == 0) {
		            tipoRepository.save(new TipoEntity(1, "administrador"));
		            tipoRepository.save(new TipoEntity(2, "cliente"));
		        }
		        if (tallaRepository.count() == 0) {
		        	tallaRepository.save(new TallaEntity(1, "L"));
		        	tallaRepository.save(new TallaEntity(2, "M"));
		        	tallaRepository.save(new TallaEntity(3, "S"));
		        }
	    	};
	    }  
	    	
}
