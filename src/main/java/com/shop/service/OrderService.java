package com.shop.service;

import com.shop.model.Address;
import com.shop.model.Orders;
import com.shop.model.User;
import org.example.common.exception.OrderException;
import org.example.common.exception.ProductException;

import java.util.List;

public interface OrderService {

    public Orders createOrder(User user, Address address) throws ProductException;
    public Orders findOrderById(Long orderId);
    public List<Orders> usersOrderHistory(Long userId);
    public Orders placedOrder(Long orderId) throws OrderException;
    public Orders confirmedOrder(Long orderId) throws OrderException;
    public Orders shippedOrder(Long orderId) throws OrderException;
    public Orders deliveredOrder(Long orderId) throws OrderException;
    public Orders canceledOrder(Long orderId) throws OrderException;
    public List<Orders> getAllOrder();
    public void deleteOrder(Long orderId) throws OrderException;
}
