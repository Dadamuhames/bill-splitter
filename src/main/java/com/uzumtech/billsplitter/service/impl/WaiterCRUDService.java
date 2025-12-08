package com.uzumtech.billsplitter.service.impl;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.dto.request.waiter.WaiterCreateRequest;
import com.uzumtech.billsplitter.dto.request.waiter.WaiterUpdateRequest;
import com.uzumtech.billsplitter.dto.response.WaiterResponse;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.exception.UniqueFieldException;
import com.uzumtech.billsplitter.exception.WaiterNotFoundException;
import com.uzumtech.billsplitter.mapper.WaiterMapper;
import com.uzumtech.billsplitter.repository.WaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaiterCRUDService {
    private final WaiterRepository waiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final WaiterMapper waiterMapper;

    public Page<WaiterResponse> list(final MerchantEntity merchant, final Pageable pageable) {
        Page<WaiterEntity> waiters = waiterRepository.findAllByMerchantId(merchant.getId(), pageable);

        return waiters.map(waiterMapper::entityToResponse);
    }

    private void validateRequest(final WaiterCreateRequest request) {
        boolean loginExists = waiterRepository.existsByLogin(request.login());

        if (loginExists) throw new UniqueFieldException(Error.WAITER_LOGIN_EXISTS_CODE);
    }

    private void validateRequest(final Long id, final WaiterUpdateRequest request) {
        boolean loginExists = waiterRepository.existsByLoginAndIdNot(request.login(), id);

        if (loginExists) throw new UniqueFieldException(Error.WAITER_LOGIN_EXISTS_CODE);
    }

    public WaiterResponse create(final WaiterCreateRequest request, final MerchantEntity merchant) {
        validateRequest(request);

        String password = passwordEncoder.encode(request.password());

        WaiterEntity waiter = waiterMapper.requestToEntity(request, merchant, password);

        waiter = waiterRepository.save(waiter);

        return waiterMapper.entityToResponse(waiter);
    }

    public WaiterEntity getInstance(final Long id, final Long merchantId) {
        return waiterRepository.findByIdAndMerchantId(id, merchantId).orElseThrow(
            () -> new WaiterNotFoundException(Error.WAITER_NOT_FOUND_CODE)
        );
    }

    public WaiterResponse update(final Long id, final WaiterUpdateRequest request, final MerchantEntity merchant) {
        validateRequest(id, request);

        WaiterEntity waiter = getInstance(id, merchant.getId());

        String password = passwordEncoder.encode(request.password());

        waiterMapper.update(request, waiter, password);

        waiterRepository.save(waiter);

        return waiterMapper.entityToResponse(waiter);
    }
}
