package com.example.Shop.service.impl;
import com.example.Shop.Model.Category;
import com.example.Shop.Model.Product;
import com.example.Shop.Model.ProductReview;
import com.example.Shop.dto.ProductDTO;
import com.example.Shop.dto.SimpleProductDTO;
import com.example.Shop.repository.CategoryRepository;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.repository.ProductReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

	@Autowired
	private  ProductRepository productRepository;

	@Autowired
	private  ProductReviewRepository productReviewRepository;

	@Autowired
	private  CategoryRepository categoryRepository;




	private Product toEntity(ProductDTO dto) {
		Product product = new Product();
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setName(dto.getName());
		product.setStockQuantity(dto.getStockQuantity());
		return product;
	}


	private List<ProductReview> findReviewsByIds(List<Long> productReviewIds) {
		return productReviewIds.stream()
				.map(id -> productReviewRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("ProductReview not found with id: " + id)))
				.collect(Collectors.toList());
	}

	public Product addProduct(ProductDTO productDTO){
		List <ProductReview> productReviews = findReviewsByIds(productDTO.getProductReviewIds());
		Category category = categoryRepository.findById(productDTO.getCategoryId())
				.orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + productDTO.getCategoryId()));
		Product product = convertToEntity(productDTO, productReviews, category);
		return productRepository.save(product);
	}

	@Transactional
	public void assignCategoryToProduct(Long productId, Long categoryId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Hotel not found"));
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Facility not found"));

		product.setCategory(category);
		productRepository.save(product);
	}


	@Transactional
	public void assignReviewsToProduct(Long productId, Long categoryId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Hotel not found"));
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Facility not found"));

		product.setCategory(category);
		productRepository.save(product);
	}


	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	public Product createProduct(SimpleProductDTO simpleProductDTO) {
		Product product = new Product();

		product.setName(simpleProductDTO.getName());
		product.setDescription(simpleProductDTO.getDescription());
		product.setPrice(simpleProductDTO.getPrice());
		product.setStockQuantity(simpleProductDTO.getStockQuantity());

		// Save the product
		return productRepository.save(product);
	}

	public Product updateProduct(Long id, Product updatedProduct) {

		Optional<Product> existingProductOptional = productRepository.findById(id);
		if (existingProductOptional.isEmpty()) {
			return null;
		}

		Product existingProduct = existingProductOptional.get();
		existingProduct.setName(updatedProduct.getName());
		existingProduct.setDescription(updatedProduct.getDescription());
		existingProduct.setPrice(updatedProduct.getPrice());
		existingProduct.setStockQuantity(updatedProduct.getStockQuantity());

		return productRepository.save(existingProduct);
	}

	private Product convertToEntity(ProductDTO productDto, List<ProductReview> reviews, Category category) {
		Product product = new Product();
		product.setCategory(category);
		product.setStockQuantity(productDto.getStockQuantity());
		product.setPrice(product.getPrice());
		product.setName(productDto.getName());
		product.setReviews(reviews);
		product.setDescription(productDto.getDescription());
		return product;
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
