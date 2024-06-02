package com.example.Shop.controller;

import com.example.Shop.Model.Product;
import com.example.Shop.Model.ProductReview;
import com.example.Shop.dto.ProductDTO;
import com.example.Shop.dto.SimpleProductDTO;
import com.example.Shop.service.impl.ProductService;
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
@RequestMapping("/products")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		try {
			List<Product> products = productService.getAllProducts();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving products");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
		try {
			Optional<Product> product = productService.getProductById(id);
			if (product.isPresent()) {
				return ResponseEntity.ok(product.get());
			} else {
				String errorMessage = "Product with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			String errorMessage = "Error retrieving product by ID: " + e.getMessage();
			logger.error(errorMessage, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
		try {
			productService.addProduct(productDTO);
			return ResponseEntity.ok(productDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product");
		}
	}


	@PostMapping("/simple")
	public ResponseEntity<?> createSimpleProduct(@RequestBody SimpleProductDTO simpleProductDTO) {
		try {
			productService.createProduct(simpleProductDTO);
			return ResponseEntity.ok(simpleProductDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating simple product");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product updatedProduct) {
		try {
			Product existingProduct = productService.getProductById(id)
					.orElse(null);
			if (existingProduct != null) {
				Product updatedProductResult = productService.updateProduct(id, updatedProduct);
				return ResponseEntity.ok(updatedProductResult);
			} else {
				String errorMessage = "Product with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Error updating product", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product");
		}
	}

	@PostMapping("/{productId}/category")
	public ResponseEntity<?> assignCategoryToProduct(@PathVariable Long productId, @RequestBody Long categoryId) {
		try {
			productService.assignCategoryToProduct(productId, categoryId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning category to product");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
		try {
			Optional<Product> productOptional = productService.getProductById(id);
			if (productOptional.isPresent()) {
				productService.deleteProduct(id);
				return ResponseEntity.noContent().build();
			} else {
				String errorMessage = "Product with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Error deleting product", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
