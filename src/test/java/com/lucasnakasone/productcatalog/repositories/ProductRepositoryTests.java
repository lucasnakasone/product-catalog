package com.lucasnakasone.productcatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.lucasnakasone.productcatalog.entities.Product;
import com.lucasnakasone.productcatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 100L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		long existingId = 1L;
		Optional <Product> result = repository.findById(existingId);
		repository.deleteById(existingId);
		result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	
	/*
	//public void deleteShouldThrowIllegalArgumentExceptionWhenIdDoesNotExist()
	// Método delete do repositorio não lança mais exceção quando o ID não existe, nesse caso o teste não valida
	@Test 
	public void deleteShouldThrowIllegalArgumentExceptionWhenIdDoesNotExist() {
		long nonExistingId = 1000L;
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
	*/
	
	@Test
	public void saveShouldPersisWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalProductWhenIdExists() {
		Optional <Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalProductWhenIdDoesNotExists() {
		Optional <Product> result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());		
	}
	
}
