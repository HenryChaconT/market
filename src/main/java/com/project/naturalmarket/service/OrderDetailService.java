package com.project.naturalmarket.service;

import com.project.naturalmarket.dto.OrderDetailDto;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetailDto> createOrderDetail(List<OrderDetailDto>  orderDetailList, long userId);

    List<OrderDetailDto> getAll();

    List<OrderDetailDto> getById(long id);

    List<OrderDetailDto> getByOrderId(long id);

    OrderDetailDto update(OrderDetailDto orderDetail,long id);

    void delete(long id);

}
