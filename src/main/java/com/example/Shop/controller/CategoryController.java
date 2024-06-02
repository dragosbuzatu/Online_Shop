package com.example.Shop.controller;

import com.example.Shop.Model.Category;
import com.example.Shop.Model.Order;
import com.example.Shop.service.impl.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<?> getAllCategories() {
		try {
			List<Category> categories = categoryService.getAllCategories();
			return ResponseEntity.ok(categories);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving categories");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
		try {
			Optional<Category> category = categoryService.getCategoryById(id);
			if (category.isPresent()) {
				return ResponseEntity.ok(category.get());
			} else {
				String errorMessage = "Category with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			String errorMessage = "Error retrieving Category by ID: " + e.getMessage();
			logger.error(errorMessage, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PostMapping
	public ResponseEntity<?> createCategory(@RequestBody Category category) {
		try {
			Category createdCategory = categoryService.createCategory(category);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating category");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody Category updatedCategory) {
		try {
			Category updatedCategoryResult = categoryService.updateCategory(id, updatedCategory);
			if (updatedCategoryResult != null) {
				return ResponseEntity.ok(updatedCategoryResult);
			} else {
				String errorMessage = "Order with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating category");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
		try {
			Optional<Category> categoryOptional = categoryService.getCategoryById(id);
			if (categoryOptional.isPresent()) {
				categoryService.deleteCategory(id);
				return ResponseEntity.noContent().build();
			} else {
				String errorMessage = "Category with ID " + id + " not found";
				logger.info(errorMessage);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			}
		} catch (Exception e) {
			logger.error("Error deleting Category", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
