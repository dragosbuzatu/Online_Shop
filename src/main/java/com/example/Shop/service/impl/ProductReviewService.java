package com.example.Shop.service.impl;

import com.example.Shop.Model.Product;
import com.example.Shop.Model.ProductReview;
import com.example.Shop.dto.ProductDTO;
import com.example.Shop.dto.ProductReviewDTO;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.repository.ProductReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductReviewService {
	private final ProductReviewRepository productReviewRepository;
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	public ProductReviewService(ProductReviewRepository productReviewRepository) {
		this.productReviewRepository = productReviewRepository;
	}

	private ProductReview toEntity(ProductReviewDTO dto) {
		ProductReview productReview = new ProductReview();
		productReview.setComment(dto.getComment());
		productReview.setRating(dto.getRating());
		Long productId = dto.getId() != null ? dto.getId() : -1L;
		productReview.setId(productId);
		return productReview;
	}

	private ProductReview convertToEntity(ProductReviewDTO dto, Product product) {
		ProductReview productReview = new ProductReview();
		productReview.setComment(dto.getComment());
		productReview.setRating(dto.getRating());
		productReview.setId(dto.getId());
		productReview.setProduct(product);
		return productReview;
	}

	public List<ProductReview> getAllProductReviews() {
		return productReviewRepository.findAll();
	}

	public Optional<ProductReview> getProductReviewById(Long id) {
		return productReviewRepository.findById(id);
	}

	public void createProductReview(ProductReviewDTO productReviewDTO) {
		Product product = productRepository.findById(productReviewDTO.getProductID()).orElseThrow(()
		 -> new EntityNotFoundException("Product not found with id: " + productReviewDTO.getProductID()));
		ProductReview productReview = convertToEntity(productReviewDTO, product);
		productReviewRepository.save(productReview);
	}

	public ProductReview updateProductReview(Long id, ProductReview updatedProductReview) {
		// Check if the review exists
		Optional<ProductReview> existingProductReviewOptional = productReviewRepository.findById(id);
		if (existingProductReviewOptional.isEmpty()) {
			// Review not found, return null or throw an exception
			return null;
		}

		// Update the existing review with the new data
		ProductReview existingProductReview = existingProductReviewOptional.get();
		existingProductReview.setRating(updatedProductReview.getRating());
		existingProductReview.setComment(updatedProductReview.getComment());

		// Save the updated review
		return productReviewRepository.save(existingProductReview);
	}

	public void deleteProductReview(Long id) {
		productReviewRepository.deleteById(id);
	}
}
