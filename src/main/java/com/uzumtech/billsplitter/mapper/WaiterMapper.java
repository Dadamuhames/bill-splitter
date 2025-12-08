package com.uzumtech.billsplitter.mapper;


import com.uzumtech.billsplitter.dto.request.waiter.WaiterCreateRequest;
import com.uzumtech.billsplitter.dto.request.waiter.WaiterUpdateRequest;
import com.uzumtech.billsplitter.dto.response.WaiterResponse;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WaiterMapper {

    WaiterResponse entityToResponse(WaiterEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encPassword")
    @Mapping(target = "merchant", source = "merchant")
    @Mapping(target = "login", source = "request.login")
    WaiterEntity requestToEntity(WaiterCreateRequest request, MerchantEntity merchant, String encPassword);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encPassword")
    @Mapping(target = "merchant", ignore = true)
    void update(WaiterUpdateRequest request, @MappingTarget WaiterEntity waiter, String encPassword);
}
