package com.demo.order.repository;

import com.demo.model.order.enums.OrderStatus;
import com.demo.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findFirstByStoreIdAndStatusOrderByIdAsc(Long storeId, OrderStatus status);
}
