package com.example.Shop.service.impl;
import com.example.Shop.Model.Category;
import com.example.Shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Optional<Category> getCategoryById(Long id) {
		return categoryRepository.findById(id);
	}

	public Category createCategory(Category category) {
		return categoryRepository.save(category);
	}

	public Category updateCategory(Long id, Category updatedCategory) {
		// Check if the category exists
		Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
		if (existingCategoryOptional.isEmpty()) {
			// Category not found, return null or throw an exception
			return null;
		}

		// Update the existing category with the new data
		Category existingCategory = existingCategoryOptional.get();
		existingCategory.setName(updatedCategory.getName());

		// Save the updated category
		return categoryRepository.save(existingCategory);
	}

	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id);
	}
}

