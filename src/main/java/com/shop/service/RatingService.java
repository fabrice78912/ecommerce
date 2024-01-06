package com.shop.service;

import com.shop.model.Rating;
import com.shop.model.User;
import com.shop.request.RatingRequest;
import org.example.common.exception.ProductException;

import java.util.List;

public interface RatingService {

    public Rating createRating(RatingRequest req, User user) throws ProductException;
    public List<Rating> getProductRating(Long productId);
}
