package com.hackerrank.eshopping.product.dashboard.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.eshopping.product.dashboard.exception.BadRequestException;
import com.hackerrank.eshopping.product.dashboard.exception.ResourceNotFoundException;
import com.hackerrank.eshopping.product.dashboard.model.Product;
import com.hackerrank.eshopping.product.dashboard.repository.ProductRepository;

@RestController
public class ProductsController {
	
	@Autowired
	ProductRepository productRepository;
	
	@RequestMapping(method = RequestMethod.POST, value = "/products")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		if(productRepository.findById(product.getId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
		productRepository.save(product);
		return new ResponseEntity<String>(HttpStatus.CREATED);
		
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/products/{product_id}")
	public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable("product_id") long id) throws  BadRequestException{
		Product currentProduct  = productRepository.findById(id)
		        .orElseThrow(() -> new BadRequestException("Product not found :: " + id));
		
		currentProduct.setRetailPrice(product.getRetailPrice());
		currentProduct.setDiscountedPrice(product.getDiscountedPrice());
		currentProduct.setAvailability(product.getAvailability());
		final Product updatedProduct = productRepository.save(currentProduct);
		return ResponseEntity.ok(updatedProduct);
				
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/products")
	public List<Product> getAllProducts() {
		List<Product> productList = productRepository.findAll();
		return productList;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/products/{product_id}")
	public ResponseEntity<Product> getProductById(@PathVariable("product_id") long id) throws Exception{
		Product neededProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found :: " + id));
		return ResponseEntity.ok().body(neededProduct);	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/products", params = {"category"})
	public ResponseEntity<List<Product>> getProductsByCatagory(@RequestParam(name = "category") String category) throws ResourceNotFoundException{
		List<Product> neededProductList = productRepository.findByCategory(category);
		
		if(null == neededProductList || neededProductList.size() == 0 ){
			return new ResponseEntity<List<Product>>( new ArrayList<Product>(), HttpStatus.OK);
		}
		List<Product> sortedList = neededProductList.stream()
						.sorted(Comparator.comparing(Product::getAvailability,Comparator.reverseOrder())
								.thenComparing(Comparator.comparing(Product::getDiscountedPrice,Comparator.naturalOrder())) 
								.thenComparing(Comparator.comparing(Product::getId,Comparator.naturalOrder())))
						.collect(Collectors.toList());
		
		
		return new ResponseEntity<List<Product>>(sortedList
				, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/products", params = {"category", "availability"})
	public ResponseEntity<List<Product>> getProductsByCatagoryAndAvailability(@RequestParam(name = "category") String category , @RequestParam(name = "availability") int availability) throws BadRequestException{
		boolean avail = (availability == 0) ? false : true;
		String categoryAfter;
		try {
			categoryAfter = URLDecoder.decode(category, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			 throw(new BadRequestException("Product not found :: " + category));
		}
		List<Product> neededProductList = productRepository.findByCategoryAndAvailability(categoryAfter, avail);
		
		if(null == neededProductList || neededProductList.size() == 0 ){
			return new ResponseEntity<List<Product>>( new ArrayList<Product>(), HttpStatus.OK);
		}
		
		List<Product> sortedList = neededProductList.stream()
				.sorted(Comparator.comparingInt(this::getDiscountPercentage).reversed()
						.thenComparingDouble(Product::getDiscountedPrice)
								.thenComparingLong(Product::getId))
				.collect(Collectors.toList());
		
			
		return new ResponseEntity<List<Product>>(sortedList, HttpStatus.OK);
	}
	
	public int getDiscountPercentage(Product p){
		double retailPrice = p.getRetailPrice();
		return (int)Math.round(((retailPrice - p.getDiscountedPrice()) / retailPrice  * 100));
	}

	


}
