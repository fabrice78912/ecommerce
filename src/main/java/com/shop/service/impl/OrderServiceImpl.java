package com.shop.service.impl;

import com.shop.model.*;
import com.shop.repo.*;
import com.shop.service.CartItemService;
import com.shop.service.CartService;
import com.shop.service.OrderService;
import com.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.example.common.exception.OrderException;
import org.example.common.exception.ProductException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ProductService productService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Override
    public Orders createOrder(User user, Address shippAddress) throws ProductException {

        shippAddress.setUser(user);
        Address address = addressRepository.save(shippAddress);
        user.getAddressList().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);
        }

        Orders createdOrder = new Orders();

        createdOrder.setUser(user);
        createdOrder.setOrderItemList(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetais().setPaymentStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());

        Orders savedOrder = orderRepository.save(createdOrder);

        for (OrderItem item : orderItems) {
            item.setOrders(savedOrder);
            orderItemRepository.save(item);
        }
        return savedOrder;
    }

    @Override
    public Orders findOrderById(Long orderId) {

        Optional<Orders> opt = orderRepository.findById(orderId);

        if (opt.isPresent()) {
            return opt.get();
        }
        throw new OrderException("Order not exist with id " + orderId);
    }

    @Override
    public List<Orders> usersOrderHistory(Long userId) {

        List<String> status= Arrays.asList("CONFIRMED", "PACED","PENDING","SHIPPED");
        List<Orders> orders= orderRepository.findByUserIdAndOrderStatusIn(userId, status);
        return orders;
    }

    @Override
    public Orders placedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Orders confirmedOrder(Long orderId) throws OrderException {
        Orders order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Orders shippedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Orders deliveredOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Orders canceledOrder(Long orderId) throws OrderException {

        Orders order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Orders> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
            Orders order= findOrderById(orderId);
            orderRepository.deleteById(orderId);
    }
}
