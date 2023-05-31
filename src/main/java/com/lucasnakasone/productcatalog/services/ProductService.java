package com.lucasnakasone.productcatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.lucasnakasone.productcatalog.dto.ProductDTO;
import com.lucasnakasone.productcatalog.entities.Product;
import com.lucasnakasone.productcatalog.repositories.ProductRepository;
import com.lucasnakasone.productcatalog.services.exceptions.DataIntegrityException;
import com.lucasnakasone.productcatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	public Page<ProductDTO> findAllPaged(PageRequest  pageRequest){
		Page<Product> list = repository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("ID not found"));
		return new ProductDTO(entity, entity.getCategories());
	}
	
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		//entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			//entity.setName(dto.getName());
			entity = repository.save(entity);
			return new ProductDTO(entity);
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