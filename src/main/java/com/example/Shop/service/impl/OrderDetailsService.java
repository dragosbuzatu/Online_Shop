package com.example.Shop.service.impl;
import com.example.Shop.Model.Category;
import com.example.Shop.Model.Order;
import com.example.Shop.Model.OrderDetails;
import com.example.Shop.Model.Product;
import com.example.Shop.dto.OrderDetailsDTO;
import com.example.Shop.repository.OrderDetailsRepository;
import com.example.Shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailsService {
	@Autowired
	private  OrderDetailsRepository orderDetailsRepository;

	@Autowired
	private  OrderRepository orderRepository;

	@Autowired
	public OrderDetailsService(OrderDetailsRepository orderDetailsRepository) {
		this.orderDetailsRepository = orderDetailsRepository;
	}


	private OrderDetails toEntity(OrderDetailsDTO dto, Order order) {
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setId(dto.getId());
		orderDetails.setPrice(dto.getPrice());
		orderDetails.setQuantity(dto.getQuantity());
		orderDetails.setOrder(order);
		return orderDetails;
	}


	public List<OrderDetails> getAllOrderItems() {
		return orderDetailsRepository.findAll();
	}

	public Optional<OrderDetails> getOrderItemById(Long id) {
		return orderDetailsRepository.findById(id);
	}

	public void createOrderItem(OrderDetailsDTO orderDetailsDTO) {
		Order order = orderRepository.findById(orderDetailsDTO.getOrderId())
				.orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderDetailsDTO.getOrderId()));
		OrderDetails orderDetails = toEntity(orderDetailsDTO, order);
		orderDetailsRepository.save(orderDetails);
	}

	@Transactional
	public void assignOrderToOrderDetails(Long orderDetailsId, Long orderId) {
		OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
				.orElseThrow(() -> new RuntimeException("OrderDetails not found"));
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));

		orderDetails.setOrder(order);
		orderDetailsRepository.save(orderDetails);
	}

	public OrderDetails updateOrderItem(Long id, OrderDetails updatedOrderDetails) {
		Optional<OrderDetails> existingOrderItemOptional = orderDetailsRepository.findById(id);
		if (existingOrderItemOptional.isEmpty()) {
			return null;
		}

		OrderDetails existingOrderDetails = existingOrderItemOptional.get();
		existingOrderDetails.setOrder(updatedOrderDetails.getOrder());
		existingOrderDetails.setQuantity(updatedOrderDetails.getQuantity());
		existingOrderDetails.setPrice(updatedOrderDetails.getPrice());

		return orderDetailsRepository.save(existingOrderDetails);
	}

	public void deleteOrderItem(Long id) {
		orderDetailsRepository.deleteById(id);
	}
}

