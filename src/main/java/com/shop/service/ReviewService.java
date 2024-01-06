package com.shop.service;


import com.shop.model.Review;
import com.shop.model.User;
import com.shop.request.ReviewRequest;
import org.example.common.exception.ProductException;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user) throws ProductException;
    public List<Review> getAllReview(Long productId) throws ProductException;
}
