package com.uzumtech.billsplitter.mapper;


import com.uzumtech.billsplitter.dto.response.TableResponse;
import com.uzumtech.billsplitter.entity.TableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TableMapper {
    TableResponse entityToResponse(TableEntity entity);
}
