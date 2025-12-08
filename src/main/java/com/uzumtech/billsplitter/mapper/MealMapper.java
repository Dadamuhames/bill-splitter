package com.uzumtech.billsplitter.mapper;


import com.uzumtech.billsplitter.dto.response.MealResponse;
import com.uzumtech.billsplitter.entity.MealEntity;
import com.uzumtech.billsplitter.utils.CurrencyConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MealMapper {
    @Autowired
    CurrencyConverter currencyConverter;

    @Mapping(target = "price", expression = "java(currencyConverter.tiyinToUzs(entity.getPrice()))")
    public abstract MealResponse entityToResponse(MealEntity entity);
}
