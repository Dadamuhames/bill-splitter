package com.uzumtech.billsplitter.service.impl;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.dto.response.bill.BillLineDto;
import com.uzumtech.billsplitter.dto.response.bill.BillResponse;
import com.uzumtech.billsplitter.dto.response.bill.GuestBillDto;
import com.uzumtech.billsplitter.entity.order.GuestEntity;
import com.uzumtech.billsplitter.entity.order.OrderItemEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.exception.NoGuestsException;
import com.uzumtech.billsplitter.mapper.OrderMapper;
import com.uzumtech.billsplitter.repository.GuestRepository;
import com.uzumtech.billsplitter.repository.OrderItemRepository;
import com.uzumtech.billsplitter.service.BillService;
import com.uzumtech.billsplitter.service.CommissionService;
import com.uzumtech.billsplitter.service.impl.order.OrderHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final OrderHelperService orderHelperService;
    private final OrderItemRepository orderItemRepository;
    private final CommissionService commissionService;
    private final GuestRepository guestRepository;
    private final OrderMapper orderMapper;

    public BillResponse splitBills(final Long orderId, final WaiterEntity waiter) {
        orderHelperService.getInstance(orderId, waiter.getId());

        int guestCountInt = guestRepository.countByOrderId(orderId);

        if (guestCountInt == 0) {
            throw new NoGuestsException(Error.ORDER_HAS_NO_GUESTS_CODE);
        }

        BigDecimal guestCount = new BigDecimal(guestCountInt);

        List<OrderItemEntity> orderItems = orderItemRepository.findAllByOrderId(orderId);

        BigDecimal commissionPerGuest = getCommissionPerGuest(guestCount, orderItems, waiter.getMerchant().getId());


        List<BillLineDto> sharedBillLines = getSharedBillLines(orderItems, guestCount);

        BigDecimal sharedTotal = sharedBillLines.stream()
            .map(BillLineDto::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add).add(commissionPerGuest);


        Map<Long, List<OrderItemEntity>> orderItemsNotShared = orderItems.stream().filter(
            o -> o.getGuest() != null).collect(Collectors.groupingBy(
            item -> item.getGuest().getId()));

        if (orderItemsNotShared.isEmpty()) {
            List<GuestEntity> allGuests = guestRepository.findAllByOrderId(orderId);

            List<GuestBillDto> bills = allGuests.stream()
                .map(guest -> new GuestBillDto(
                    guest.getId(),
                    new ArrayList<>(sharedBillLines),
                    commissionPerGuest,
                    sharedTotal,
                    OffsetDateTime.now()
                ))
                .toList();

            return new BillResponse(bills);
        }


        List<GuestBillDto> bills = new ArrayList<>();

        orderItemsNotShared.forEach((guestId, guestItems) -> {
            List<BillLineDto> billLines = new ArrayList<>(sharedBillLines);
            BigDecimal total = sharedTotal;

            for (var orderItem : guestItems) {
                var billLine = orderMapper.orderItemToBillLinePrivate(orderItem);
                billLines.add(billLine);
                total = total.add(billLine.subtotal());
            }

            bills.add(new GuestBillDto(guestId, billLines, commissionPerGuest, total, OffsetDateTime.now()));
        });

        return new BillResponse(bills);
    }


    private List<BillLineDto> getSharedBillLines(final List<OrderItemEntity> orderItems, final BigDecimal guestCount) {
        List<OrderItemEntity> orderItemsShared = orderItems.stream().filter(o -> o.getGuest() == null).toList();

        return orderItemsShared.stream()
            .map(item -> orderMapper.orderItemToBillLineShared(item, guestCount))
            .toList();
    }

    private BigDecimal getCommissionPerGuest(final BigDecimal guestCount, final List<OrderItemEntity> orderItems, final Long merchantId) {
        BigDecimal orderTotal = orderHelperService.calculateTotalPrice(orderItems);
        BigDecimal merchantCommission = commissionService.getMerchantCommissionRate(merchantId);
        BigDecimal commission = orderTotal.multiply(merchantCommission);
        return commission.divide(guestCount, RoundingMode.HALF_UP);
    }
}
