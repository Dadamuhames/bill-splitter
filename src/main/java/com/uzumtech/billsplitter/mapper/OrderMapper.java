package com.uzumtech.billsplitter.mapper;


import com.uzumtech.billsplitter.dto.response.bill.BillLineDto;
import com.uzumtech.billsplitter.dto.response.order.GuestResponse;
import com.uzumtech.billsplitter.dto.response.order.OrderDetailResponse;
import com.uzumtech.billsplitter.dto.response.order.OrderItemDto;
import com.uzumtech.billsplitter.dto.response.order.OrderResponse;
import com.uzumtech.billsplitter.entity.TableEntity;
import com.uzumtech.billsplitter.entity.order.OrderEntity;
import com.uzumtech.billsplitter.entity.order.OrderItemEntity;
import com.uzumtech.billsplitter.repository.projection.OrderProjection;
import com.uzumtech.billsplitter.utils.CurrencyConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OrderMapper {
    @Autowired
    CurrencyConverter currencyConverter;
    @Autowired
    TableMapper tableMapper;
    @Autowired
    MealMapper mealMapper;

    @Mapping(target = "table.id", expression = "java(orderProjection.getTableId())")
    @Mapping(target = "table.tableNumber", expression = "java(orderProjection.getTableNumber())")
    @Mapping(target = "price", expression = "java(currencyConverter.tiyinToUzs(orderProjection.getPrice()))")
    public abstract OrderResponse projectionToResponse(OrderProjection orderProjection);


    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "price", source = "totalPrice")
    @Mapping(target = "table", expression = "java(tableMapper.entityToResponse(table))")
    public abstract OrderResponse entityToResponse(
        OrderEntity order,
        TableEntity table,
        BigDecimal totalPrice
    );


    @Mapping(target = "table", expression = "java(tableMapper.entityToResponse(order.getTable()))")
    @Mapping(target = "price", source = "totalPrice")
    @Mapping(target = "guests", source = "guests")
    @Mapping(target = "sharedMeals", source = "sharedMeals")
    public abstract OrderDetailResponse entityToDetailResponse(
        OrderEntity order,
        BigDecimal totalPrice,
        List<GuestResponse> guests,
        List<OrderItemDto> sharedMeals
    );


    @Mapping(target = "meal", expression = "java(mealMapper.entityToResponse(entity.getMeal()))")
    public abstract OrderItemDto orderItemToDto(OrderItemEntity entity);



    public BillLineDto orderItemToBillLinePrivate(final OrderItemEntity orderItem) {
        BigDecimal mealPrice = currencyConverter.tiyinToUzs(orderItem.getMealPrice());
        BigDecimal subtotal = mealPrice.multiply(new BigDecimal(orderItem.getQuantity()));

        return new BillLineDto(orderItem.getMeal().getId(), orderItem.getMeal().getName(), mealPrice, orderItem.getQuantity(), subtotal);
    }

    public BillLineDto orderItemToBillLineShared(final OrderItemEntity orderItem, final BigDecimal guestCount) {
        BigDecimal mealPrice = currencyConverter.tiyinToUzs(orderItem.getMealPrice());
        BigDecimal subtotal = mealPrice.multiply(new BigDecimal(orderItem.getQuantity())).divide(guestCount, RoundingMode.HALF_UP);

        return new BillLineDto(orderItem.getMeal().getId(), orderItem.getMeal().getName(), mealPrice, orderItem.getQuantity(), subtotal);
    }
}
