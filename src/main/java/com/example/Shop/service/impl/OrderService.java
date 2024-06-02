package com.example.Shop.service.impl;

import com.example.Shop.Model.Order;
import com.example.Shop.Model.OrderDetails;
import com.example.Shop.Model.Product;
import com.example.Shop.dto.OrderDTO;
import com.example.Shop.repository.OrderDetailsRepository;
import com.example.Shop.repository.OrderRepository;
import com.example.Shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

	private static Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private  OrderRepository orderRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	public Order createOrder(OrderDTO orderDTO) {
		List<Product> products = findProductsByIds(orderDTO.getProductId());
		Order order = convertToEntity(orderDTO, products);
		for (Product product : products) {
			product.setOrder(order);
		}

		return orderRepository.save(order);
	}

	@Transactional
	public void assignOrderDetailsToOrder(Long orderId, Long orderDetailsId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
				.orElseThrow(() -> new RuntimeException("OrderDetails not found"));

		order.setOrderDetails(orderDetails);
		orderRepository.save(order);
	}

	private List<Product> findProductsByIds(List<Long> productsIds) {
		return productsIds.stream()
				.map(id -> productRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Product not found with id: " + id)))
				.collect(Collectors.toList());
	}

	private Order convertToEntity(OrderDTO orderDTO, List<Product> productsIds) {
		Order order = new Order();
		order.setOrderDate(orderDTO.getOrderDate());
		order.setTotalAmount(orderDTO.getTotalAmount());
		order.setId(orderDTO.getId());
		List<Product> products = productsIds.stream()
				.map(productId -> productRepository.findById(productId.getId())
						.orElseThrow(() -> new RuntimeException("Product not found with id: " + productId)))
				.collect(Collectors.toList());
		order.setProducts(products);

		return order;
	}

	public Order updateOrder(Long id, Order updatedOrder) {
		// Check if the order exists
		Optional<Order> existingOrderOptional = orderRepository.findById(id);
		if (existingOrderOptional.isEmpty()) {
			// Order not found, return null or throw an exception
			return null;
		}

		// Update the existing order with the new data
		Order existingOrder = existingOrderOptional.get();
		existingOrder.setOrderDate(updatedOrder.getOrderDate());
		existingOrder.setTotalAmount(updatedOrder.getTotalAmount());

		// Save the updated order
		return orderRepository.save(existingOrder);
	}

	public void deleteOrder(Long id) {
		orderRepository.deleteById(id);
	}
}
