package com.lucasnakasone.productcatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.lucasnakasone.productcatalog.dto.CategoryDTO;
import com.lucasnakasone.productcatalog.entities.Category;
import com.lucasnakasone.productcatalog.repositories.CategoryRepository;
import com.lucasnakasone.productcatalog.services.exceptions.DataIntegrityException;
import com.lucasnakasone.productcatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	public Page<CategoryDTO> findAllPaged(PageRequest  pageRequest){
		Page<Category> list = repository.findAll(pageRequest);
		return list.map(x -> new CategoryDTO(x));
	}

	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("ID not found"));
		return new CategoryDTO(entity);
	}

	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found");
		}
	}
	
	public void delete(Long id) {
		if (repository.existsById(id)) {
			try {
				repository.deleteById(id);
			} catch (DataIntegrityViolationException e) {
				throw new DataIntegrityException(e.getMessage());
			}
		} else {
			throw new ResourceNotFoundException("ID not found");
		}
	}
	
}