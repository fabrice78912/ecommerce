package com.shop.service.impl;

import com.shop.model.Product;
import com.shop.model.Review;
import com.shop.model.User;
import com.shop.repo.ProductRepository;
import com.shop.repo.ReviewRepository;
import com.shop.request.ReviewRequest;
import com.shop.service.ProductService;
import com.shop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.example.common.exception.ProductException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;


    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        Product product = productService.findProductById(req.getProductId());

        //Create review
        Review review = new Review();
        review.setUser(user);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());

        //Save and return Review
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReview(Long productId) throws ProductException {
        return reviewRepository.getAllProductsReview(productId);
    }
}
