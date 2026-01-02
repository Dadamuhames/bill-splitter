package com.uzumtech.billsplitter.service.impl.order;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.entity.TableEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.exception.TableNotAvailableException;
import com.uzumtech.billsplitter.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;

    public TableEntity getTableIfFree(final Long id, final WaiterEntity waiter) {
        return tableRepository.findFreeByIdAndMerchantId(id, waiter.getMerchant().getId())
            .orElseThrow(() -> new TableNotAvailableException(Error.TABLE_NOT_AVAILABLE_CODE));
    }
}
