package com.be.app.service;

import com.be.app.dto.request.OrderInsertRequest;
import com.be.app.dto.request.OrderUpdateRequest;
import com.be.app.dto.response.BaseResponse;

public interface OrderService {

    BaseResponse saveOrder(OrderInsertRequest request);

    BaseResponse updateOrderByUUID(OrderUpdateRequest request);

    BaseResponse deleteOrderByUUID(String uuid);

    BaseResponse getOrder(int page, int limit, String search);

    BaseResponse getOrderByUUID(String uuid);

}
