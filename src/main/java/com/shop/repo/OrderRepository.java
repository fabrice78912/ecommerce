package com.shop.repo;

import com.shop.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.user.id=: userId AND (o.orderStatus='PLACED' OR o.orderStatus='CONFIRMED' " +
            " OR o.orderStatus='SHIPPED' OR o.orderStatus='PENDING')")
    List<Orders> getUserOrders(@Param("userId") Long userId);

    List<Orders> findByUserIdAndOrderStatusIn(Long userId, List<String> statuses);

}
