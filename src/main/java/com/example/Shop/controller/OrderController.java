package com.example.Shop.controller;

import com.example.Shop.Model.Order;
import com.example.Shop.Model.OrderDetails;
import com.example.Shop.dto.OrderDTO;
import com.example.Shop.service.impl.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8008")
@RestController
@RequestMapping("/orders")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public ResponseEntity<?> getAllOrders() {
		try {
			List<Order> orders = orderService.getAllOrders();
			return ResponseEntity.ok(orders);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving orders");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable("id") Long id) {
		try {
			Optional<Order> order = orderService.getOrderById(id);
			if (order.isPresent()) {
				return ResponseEntity.ok(order.get());
			} else {
				String errorMessage = "Order with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			String errorMessage = "Error retrieving Order by ID: " + e.getMessage();
			logger.error(errorMessage, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
		Order order = orderService.createOrder(orderDTO);
		return ResponseEntity.ok(order);
	}

	@PostMapping("/{orderId}/orderDetails")
	public ResponseEntity<?> assignOrderDetailsToOrder(@PathVariable Long orderId, @RequestParam Long orderDetailsId) {
		try {
			orderService.assignOrderDetailsToOrder(orderId, orderDetailsId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning order details to order");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrder(@PathVariable("id") Long id, @RequestBody Order updatedOrder) {
		try {
			Order updatedOrderResult = orderService.updateOrder(id, updatedOrder);
			if (updatedOrderResult != null) {
				return ResponseEntity.ok(updatedOrderResult);
			} else {
				String errorMessage = "Order with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating order");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
		try {
			Optional<Order> orderOptional = orderService.getOrderById(id);
			if (orderOptional.isPresent()) {
				orderService.deleteOrder(id);
				return ResponseEntity.noContent().build();
			} else {
				String errorMessage = "Order with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Error deleting Order", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
