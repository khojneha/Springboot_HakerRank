package com.hackerrank.eshopping.product.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hackerrank.eshopping.product.dashboard.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	public List<Product> findByCategory(String category);
	

    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    public List<Product> find(@Param("category") String category);
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category) AND p.availability = :availability")
    public List<Product> findByCategoryAndAvailability(@Param("category") String category, @Param("availability") boolean availability);
   }
