package com.shop.service.impl;

import com.shop.model.Product;
import com.shop.model.Rating;
import com.shop.model.User;
import com.shop.repo.RatingRepository;
import com.shop.request.RatingRequest;
import com.shop.service.ProductService;
import com.shop.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.example.common.exception.ProductException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ProductService productService;


    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {

        Product product= productService.findProductById(req.getProductId());
        Rating rating= new Rating();
        rating.setCreatedAt(LocalDateTime.now());
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRating(Long productId) {
        return ratingRepository.getAllProductRating(productId);
    }
}
