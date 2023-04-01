package com.lucasnakasone.productcatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasnakasone.productcatalog.dto.CategoryDTO;
import com.lucasnakasone.productcatalog.entities.Category;
import com.lucasnakasone.productcatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());        
	}
	
	
	
}
