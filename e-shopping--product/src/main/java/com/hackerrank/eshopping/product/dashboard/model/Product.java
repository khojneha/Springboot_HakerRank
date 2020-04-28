package com.hackerrank.eshopping.product.dashboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product{
	
    private long id;
    private String name;
    private String category;
    private double retailPrice;
    private double discountedPrice;
    private boolean availability;
   
	public Product() {
    }

    public Product(long id, String name, String category, double retailPrice, double discountedPrice, Boolean availability) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.retailPrice = retailPrice;
        this.discountedPrice = discountedPrice;
        this.availability = availability;
    }

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "category", nullable = false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("retail_price")
    @Column(name = "retail_price", nullable = false)
    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    @JsonProperty("discounted_price")
    @Column(name = "discounted_price", nullable = false)
    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    @Column(name = "availability", nullable = false)
    public boolean getAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", category=" + category + ", retailPrice=" + retailPrice
				+ ", discountedPrice=" + discountedPrice + ", availability=" + availability + "]";
	}
    
    
}
