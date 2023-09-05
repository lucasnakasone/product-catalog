package com.lucasnakasone.productcatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lucasnakasone.productcatalog.dto.ProductDTO;
import com.lucasnakasone.productcatalog.entities.Product;
import com.lucasnakasone.productcatalog.repositories.ProductRepository;
import com.lucasnakasone.productcatalog.services.exceptions.DataIntegrityException;
import com.lucasnakasone.productcatalog.services.exceptions.ResourceNotFoundException;
import com.lucasnakasone.productcatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long depententId;
	private PageImpl<Product> page;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		depententId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		doNothing().when(repository).deleteById(existingId);
		doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityException.class).when(repository).deleteById(depententId);
		when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
		verify(repository, times(1)).findById(existingId);
	}
	
	//@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		verify(repository, times(1)).findById(nonExistingId);
	}
	
	@Test
	public void deleteShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		verify(repository, times(1)).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		verify(repository, times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		verify(repository, times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowDataIntegrityExceptionWhenDepententId() {
		Assertions.assertThrows(DataIntegrityException.class, () -> {
			service.delete(depententId);
		});
		verify(repository, times(1)).deleteById(depententId);
	}
	
}
