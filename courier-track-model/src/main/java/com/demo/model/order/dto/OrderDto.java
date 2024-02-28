package com.demo.model.order.dto;


import com.demo.model.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private Long storeId;
    private OrderLocationDto orderLocation;
    private OrderStatus status;
}
