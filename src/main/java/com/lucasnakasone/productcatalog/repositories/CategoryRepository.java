package com.lucasnakasone.productcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucasnakasone.productcatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
}
