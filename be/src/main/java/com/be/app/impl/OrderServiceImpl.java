package com.be.app.impl;

import com.be.app.dto.request.OrderItemRequest;
import com.be.app.dto.request.OrderInsertRequest;
import com.be.app.dto.request.OrderUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.entity.OrderEntity;
import com.be.app.entity.OrderItemEntity;
import com.be.app.repository.OrderDetailRepository;
import com.be.app.repository.OrderRepository;
import com.be.app.service.OrderService;
import com.be.constanta.ResponseMessagesConst;
import com.be.constanta.StatusConst;
import com.be.handler.InternalServerErrorHandler;
import com.be.helper.DateHelper;
import com.be.helper.NullEmptyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public BaseResponse saveOrder(OrderInsertRequest listOrderRequest) {
        try {
            OrderEntity newOrder = new OrderEntity();
            newOrder.setUuid(UUID.randomUUID().toString());
            newOrder.setUsername(listOrderRequest.getUsername());
            newOrder.setSubTotal(listOrderRequest.getSubTotal());
            newOrder.setTotalNet(listOrderRequest.getTotalNet());
            newOrder.setTax(listOrderRequest.getTax());
            newOrder.setServiceCharge(listOrderRequest.getServiceCharge());
            newOrder.setStatus(StatusConst.WAITING.toString());

            Timestamp dateNow = DateHelper.getTimestampNow();

            newOrder.setCreatedAt(dateNow);
            newOrder.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            newOrder.setModifiedAt(dateNow);
            newOrder.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

            newOrder = orderRepository.save(newOrder);

            List<OrderItemEntity> listItem = new ArrayList<>();
            for (OrderItemRequest item : listOrderRequest.getItem()) {
                OrderItemEntity itemEntity = new OrderItemEntity();
                itemEntity.setOrderID(newOrder.getId());
                itemEntity.setUuid(newOrder.getUuid());
                itemEntity.setProductID(item.getProductID());
                itemEntity.setQuantity(item.getQuantity());
                itemEntity.setTotalPrice(item.getTotalPrice());
                itemEntity.setPrice(item.getPrice());

                listItem.add(itemEntity);
            }

            orderDetailRepository.saveAll(listItem);

            return new BaseResponse(true, ResponseMessagesConst.INSERT_SUCCESS.toString(), null);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse updateOrderByUUID(OrderUpdateRequest request) {
        try {
            OrderEntity oldOrder = orderRepository.findByUUID(request.getUuid());
            if (NullEmptyChecker.isNullOrEmpty(oldOrder)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString(), null);
            }

            OrderEntity updateOrder = orderRepository.findByUUID(request.getUuid());
            updateOrder.setUsername(request.getUsername());
            updateOrder.setStatus(request.getStatus());

            Timestamp dateNow = DateHelper.getTimestampNow();

            updateOrder.setModifiedAt(dateNow);
            updateOrder.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

            OrderEntity order = orderRepository.save(updateOrder);

            return new BaseResponse(true, ResponseMessagesConst.UPDATE_SUCCESS.toString(), order);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse deleteOrderByUUID(String uuid) {
        try {
            OrderEntity oldOrder = orderRepository.findByUUID(uuid);
            if (NullEmptyChecker.isNullOrEmpty(oldOrder)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString(), null);
            }

            orderRepository.delete(oldOrder);

            return new BaseResponse(true, ResponseMessagesConst.UPDATE_SUCCESS.toString(), null);
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getOrder(int page, int limit, String search) {
        try {
            List<OrderEntity> listOrder;
            if (NullEmptyChecker.isNullOrEmpty(page) || NullEmptyChecker.isNullOrEmpty(limit)) {
                listOrder = orderRepository.findAll();
            } else if (NullEmptyChecker.isNullOrEmpty(search)) {
                Pageable pageable = PageRequest.of(page, limit);
                listOrder = orderRepository.findAll(pageable).toList();
            } else {
                listOrder = orderRepository.findByUsername(search);
            }

            if (NullEmptyChecker.isNotNullOrEmpty(listOrder)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listOrder);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getOrderByUUID(String uuid) {
        try {
            OrderEntity listOrder = orderRepository.findByUUID(uuid);

            if (NullEmptyChecker.isNotNullOrEmpty(listOrder)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listOrder);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }
}
