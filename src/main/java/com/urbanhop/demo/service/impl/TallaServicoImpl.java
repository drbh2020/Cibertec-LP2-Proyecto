package com.urbanhop.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urbanhop.demo.entity.TallaEntity;
import com.urbanhop.demo.repository.TallaRepository;
import com.urbanhop.demo.service.TallaService;

@Service
public class TallaServicoImpl implements TallaService{
	
	@Autowired
	TallaRepository tallaRepository;

	@Override
	public List<TallaEntity> lstTallas() {
		List<TallaEntity> lstTallas = new ArrayList<TallaEntity>();
		return tallaRepository.findAll() != null ? tallaRepository.findAll() : lstTallas;
	}

}
