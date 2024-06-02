package com.example.Shop.dto;

import com.example.Shop.Model.Category;

import java.util.List;

public class ProductDTO {
	private String name;
	private String description;
	private double price;
	private int stockQuantity;
	private List<Long> productReviewIds;
	private Long categoryId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProductReviewIds(List<Long> productReviewIds) {
		this.productReviewIds = productReviewIds;
	}

	public List<Long> getProductReviewIds() {
		return productReviewIds;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoriyId) {
		this.categoryId = categoriyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}


	public ProductDTO() {
	}

	public ProductDTO(String name, String description, double price, int stockQuantity, List<Long> productReviewIds, Long categoryId) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.productReviewIds = productReviewIds;
		this.categoryId = categoryId;
	}
}