package com.lucasnakasone.productcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lucasnakasone.productcatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
}
