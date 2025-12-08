package com.uzumtech.billsplitter.mapper;


import com.uzumtech.billsplitter.dto.request.auth.MerchantRegistrationRequest;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantMapper {

    @Mapping(target = "password", ignore = true)
    MerchantEntity requestToEntity(final MerchantRegistrationRequest request);
}
