package com.demo.order.mapper;

import com.demo.model.order.dto.OrderDto;
import com.demo.order.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderDto orderToOrderDto(Order order);

    List<OrderDto> orderListToOrderDtoList(List<Order> orderList);
}
