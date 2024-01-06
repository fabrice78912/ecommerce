package com.shop.service;

import com.shop.model.Cart;
import com.shop.model.User;
import com.shop.request.AddItemRequest;
import org.example.common.exception.ProductException;

public interface CartService {

    public Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;
    public Cart findUserCart(Long userId) throws ProductException;
}
