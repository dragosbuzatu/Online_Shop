package com.example.Shop.controller;

import com.example.Shop.Model.ProductReview;
import com.example.Shop.dto.ProductReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Shop.service.impl.ProductReviewService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8008")
@RestController
@RequestMapping("/product-reviews")

public class ProductReviewController {

	private static final Logger logger = LoggerFactory.getLogger(ProductReviewController.class);

	@Autowired
	private final ProductReviewService productReviewService;

	@Autowired
	public ProductReviewController(ProductReviewService productReviewService) {
		this.productReviewService = productReviewService;
	}

	@GetMapping
	public ResponseEntity<?> getAllProductReviews() {
		try {
			List<ProductReview> productReviews = productReviewService.getAllProductReviews();
			return ResponseEntity.ok(productReviews);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving product reviews");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductReviewById(@PathVariable("id") Long id) {
		try {
			Optional<ProductReview> productReview = productReviewService.getProductReviewById(id);
			if (productReview.isPresent()) {
				return ResponseEntity.ok(productReview.get());
			} else {
				String errorMessage = "Product review with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			String errorMessage = "Error retrieving product review by ID: " + e.getMessage();
			logger.error(errorMessage, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PostMapping
	public ResponseEntity<?> createProductReview(@RequestBody ProductReviewDTO productReviewDTO) {
		try {
			productReviewService.createProductReview(productReviewDTO);
			return ResponseEntity.ok(productReviewDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product review");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProductReview(@PathVariable("id") Long id, @RequestBody ProductReview updatedProductReview) {
		try {
			ProductReview updatedReview = productReviewService.updateProductReview(id, updatedProductReview);
			if (updatedReview != null) {
				return ResponseEntity.ok(updatedReview);
			} else {
				String errorMessage = "Product review with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product review");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProductReview(@PathVariable("id") Long id) {
		try {
			Optional<ProductReview> productReviewOptional = productReviewService.getProductReviewById(id);
			if (productReviewOptional.isPresent()) {
				productReviewService.deleteProductReview(id);
				return ResponseEntity.noContent().build();
			} else {
				String errorMessage = "Product review with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Error deleting product review", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
