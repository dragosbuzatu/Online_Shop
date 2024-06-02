package com.example.Shop.controller;

import com.example.Shop.Model.Order;
import com.example.Shop.Model.OrderDetails;
import com.example.Shop.Model.Product;
import com.example.Shop.dto.OrderDetailsDTO;
import com.example.Shop.service.impl.OrderDetailsService;
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
@RequestMapping("/orderDetails")
public class OrderDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(OrderDetails.class);

	@Autowired
	private final OrderDetailsService orderDetailsService;

	@Autowired
	public OrderDetailsController(OrderDetailsService orderDetailsService) {
		this.orderDetailsService = orderDetailsService;
	}

	@GetMapping
	public ResponseEntity<?> getAllOrderItems() {
		try {
			List<OrderDetails> orderDetails = orderDetailsService.getAllOrderItems();
			return ResponseEntity.ok(orderDetails);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving order items");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderItemById(@PathVariable("id") Long id) {
		try {
			Optional<OrderDetails> orderDetails = orderDetailsService.getOrderItemById(id);
			if (orderDetails.isPresent()) {
				return ResponseEntity.ok(orderDetails.get());
			} else {
				String errorMessage = "Order Details with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			String errorMessage = "Error retrieving Order Details by ID: " + e.getMessage();
			logger.error(errorMessage, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PostMapping("/{orderDetailsId}/order")
	public ResponseEntity<?> assignOrderToOrderDetails(@PathVariable Long orderDetailsId, @RequestBody Long orderId) {
		try {
			orderDetailsService.assignOrderToOrderDetails(orderDetailsId, orderId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning order to order details");
		}
	}

	@PostMapping
	public ResponseEntity<?> createOrderItem(@RequestBody OrderDetailsDTO orderDetailsDTO) {
		try {
			orderDetailsService.createOrderItem(orderDetailsDTO);
			return ResponseEntity.ok(orderDetailsDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order item");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrderItem(@PathVariable("id") Long id, @RequestBody OrderDetails updatedOrderDetails) {
		try {
			OrderDetails updatedOrderDetailsResult = orderDetailsService.updateOrderItem(id, updatedOrderDetails);
			if (updatedOrderDetailsResult != null) {
				return ResponseEntity.ok(updatedOrderDetailsResult);
			} else {
				String errorMessage = "Order Details with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating order details");
		}
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrderItem(@PathVariable("id") Long id) {
		try {
			Optional<OrderDetails> orderDetailsOptional = orderDetailsService.getOrderItemById(id);
			if (orderDetailsOptional.isPresent()) {
				orderDetailsService.deleteOrderItem(id);
				return ResponseEntity.noContent().build();
			} else {
				String errorMessage = "Order Details with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Error deleting Order Details", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}


