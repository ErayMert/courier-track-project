package com.demo.order.entity;


import com.demo.model.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Orders", indexes = {
        @Index(name = "idx_orders_on_store_id_and_status", columnList = "storeId, status")
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long storeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_location_id")
    private OrderLocation orderLocation;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
