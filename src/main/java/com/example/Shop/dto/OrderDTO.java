package com.example.Shop.dto;

import com.example.Shop.Model.OrderDetails;
import com.example.Shop.Model.Product;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

	private Long id;
	private LocalDateTime orderDate;
	private double totalAmount;
	private List<Long> productId;
	private Long orderDetailsId;

	public Long getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Long orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public List<Long> getProductId() {
		return productId;
	}

	public void setProductId(List<Long> productId) {
		this.productId = productId;
	}
}
