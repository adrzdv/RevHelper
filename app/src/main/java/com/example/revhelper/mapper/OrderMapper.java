package com.example.revhelper.mapper;

import com.example.revhelper.model.dto.OrderParcelable;
import com.example.revhelper.model.order.Order;

public class OrderMapper {

    public static OrderParcelable fromOrderToParcelable(Order order) {
        return new OrderParcelable(order.getNumber(),
                order.getRoute(),
                order.getDate(),
                order.getDirectors(),
                TrainMapper.toParceFromDto(order.getTrain()));
    }

    public static Order fromParcelableToOrder(OrderParcelable order) {
        return new Order(order.getNumber(),
                order.getRoute(),
                order.getDate(),
                order.getDirectors(),
                TrainMapper.fromParcelableToDto(order.getTrain()));
    }
}
