package com.ajay.service;

import com.ajay.domain.OrderType;
import com.ajay.model.Coin;
import com.ajay.model.Order;
import com.ajay.model.OrderItem;
import com.ajay.model.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrderOfUser(Long userid, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType,User user) throws Exception;
}
