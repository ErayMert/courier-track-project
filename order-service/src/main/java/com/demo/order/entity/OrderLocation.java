package com.demo.order.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Order_Location")
public class OrderLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double latitude;

    private Double longitude;
}
