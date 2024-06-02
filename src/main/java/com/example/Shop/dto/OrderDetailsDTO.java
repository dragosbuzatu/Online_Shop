package com.example.Shop.dto;

import jakarta.persistence.Column;

public class OrderDetailsDTO {


	private Long id;
	private int quantity;
	private double price;
	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public OrderDetailsDTO(Long id, int quantity, double price, Long orderId) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.orderId = orderId;
	}
}
