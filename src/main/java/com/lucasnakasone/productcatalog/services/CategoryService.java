package com.lucasnakasone.productcatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasnakasone.productcatalog.dto.CategoryDTO;
import com.lucasnakasone.productcatalog.entities.Category;
import com.lucasnakasone.productcatalog.repositories.CategoryRepository;

import com.lucasnakasone.productcatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());        
	}

	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("ID not found"));
		return new CategoryDTO(entity);
	}

	public CategoryDTO insert(CategoryDTO obj) {
		Category entity = new Category();
		entity.setName(obj.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}
	
	
	
}
