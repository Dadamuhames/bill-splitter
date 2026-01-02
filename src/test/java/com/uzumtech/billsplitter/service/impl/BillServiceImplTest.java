package com.uzumtech.billsplitter.service.impl;

import com.uzumtech.billsplitter.constant.enums.OrderStatus;
import com.uzumtech.billsplitter.dto.response.bill.BillLineDto;
import com.uzumtech.billsplitter.dto.response.bill.BillResponse;
import com.uzumtech.billsplitter.entity.MealEntity;
import com.uzumtech.billsplitter.entity.order.*;
import com.uzumtech.billsplitter.entity.user.*;
import com.uzumtech.billsplitter.exception.NoGuestsException;
import com.uzumtech.billsplitter.mapper.OrderMapper;
import com.uzumtech.billsplitter.repository.GuestRepository;
import com.uzumtech.billsplitter.repository.OrderItemRepository;
import com.uzumtech.billsplitter.service.CommissionService;
import com.uzumtech.billsplitter.service.impl.order.OrderHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {

    @Mock
    private OrderHelperService orderHelperService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CommissionService commissionService;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private BillServiceImpl billService;

    private WaiterEntity waiter;
    private MerchantEntity merchant;
    private OrderEntity order;
    private GuestEntity guest1;
    private GuestEntity guest2;
    private MealEntity meal1;
    private MealEntity meal2;
    private OrderItemEntity sharedItem;
    private OrderItemEntity privateItem1;
    private OrderItemEntity privateItem2;

    @BeforeEach
    void setUp() {
        merchant = MerchantEntity.builder()
            .id(1L)
            .login("merchant")
            .taxNumber("123456789")
            .name("Test Merchant")
            .build();

        waiter = WaiterEntity.builder()
            .id(1L)
            .fullName("John Waiter")
            .merchant(merchant)
            .build();

        order = OrderEntity.builder()
            .id(1L)
            .waiter(waiter)
            .status(OrderStatus.OPEN)
            .build();

        guest1 = GuestEntity.builder()
            .id(1L)
            .order(order)
            .build();

        guest2 = GuestEntity.builder()
            .id(2L)
            .order(order)
            .build();

        meal1 = MealEntity.builder()
            .id(1L)
            .name("Pizza")
            .price(1000L)
            .merchant(merchant)
            .build();

        meal2 = MealEntity.builder()
            .id(2L)
            .name("Burger")
            .price(500L)
            .merchant(merchant)
            .build();

        sharedItem = OrderItemEntity.builder()
            .id(1L)
            .order(order)
            .guest(null)
            .meal(meal1)
            .mealPrice(1000L)
            .quantity(1)
            .build();

        privateItem1 = OrderItemEntity.builder()
            .id(2L)
            .order(order)
            .guest(guest1)
            .meal(meal2)
            .mealPrice(500L)
            .quantity(2)
            .build();

        privateItem2 = OrderItemEntity.builder()
            .id(3L)
            .order(order)
            .guest(guest2)
            .meal(meal2)
            .mealPrice(500L)
            .quantity(1)
            .build();
    }

    @Test
    void testSplitBills_Success() {
        List<OrderItemEntity> orderItems = List.of(sharedItem, privateItem1, privateItem2);

        when(orderHelperService.getInstance(1L, 1L)).thenReturn(order);
        when(guestRepository.countByOrderId(1L)).thenReturn(2);
        when(orderItemRepository.findAllByOrderId(1L)).thenReturn(orderItems);
        when(orderHelperService.calculateTotalPrice(orderItems)).thenReturn(new BigDecimal("2000"));
        when(commissionService.getMerchantCommissionRate(1L)).thenReturn(new BigDecimal("0.10"));

        BillLineDto sharedBillLine = new BillLineDto(1L, "Pizza", new BigDecimal("1000"), 1, new BigDecimal("500"));
        when(orderMapper.orderItemToBillLineShared(eq(sharedItem), any(BigDecimal.class)))
            .thenReturn(sharedBillLine);

        BillLineDto privateBillLine1 = new BillLineDto(2L, "Burger", new BigDecimal("500"), 2, new BigDecimal("1000"));
        when(orderMapper.orderItemToBillLinePrivate(privateItem1)).thenReturn(privateBillLine1);

        BillLineDto privateBillLine2 = new BillLineDto(2L, "Burger", new BigDecimal("500"), 1, new BigDecimal("500"));
        when(orderMapper.orderItemToBillLinePrivate(privateItem2)).thenReturn(privateBillLine2);

        BillResponse response = billService.splitBills(1L, waiter);

        assertThat(response).isNotNull();
        verify(orderHelperService).getInstance(1L, 1L);
        verify(guestRepository).countByOrderId(1L);
        verify(orderItemRepository).findAllByOrderId(1L);
        verify(commissionService).getMerchantCommissionRate(1L);
    }

    // test 2
    @Test
    void testSplitBills_NoGuests_ThrowsException() {
        when(orderHelperService.getInstance(1L, 1L)).thenReturn(order);
        when(guestRepository.countByOrderId(1L)).thenReturn(0);

        assertThatThrownBy(() -> billService.splitBills(1L, waiter))
            .isInstanceOf(NoGuestsException.class);

        verify(orderHelperService).getInstance(1L, 1L);
        verify(guestRepository).countByOrderId(1L);
        verify(orderItemRepository, never()).findAllByOrderId(any());
    }

    // test 3
    @Test
    void testSplitBills_OnlySharedItems() {
        List<OrderItemEntity> orderItems = List.of(sharedItem);
        List<GuestEntity> allGuests = List.of(guest1, guest2);

        when(orderHelperService.getInstance(1L, 1L)).thenReturn(order);
        when(guestRepository.countByOrderId(1L)).thenReturn(2);
        when(orderItemRepository.findAllByOrderId(1L)).thenReturn(orderItems);
        when(guestRepository.findAllByOrderId(1L)).thenReturn(allGuests);
        when(orderHelperService.calculateTotalPrice(orderItems)).thenReturn(new BigDecimal("1000"));
        when(commissionService.getMerchantCommissionRate(1L)).thenReturn(new BigDecimal("0.10"));

        BillLineDto sharedBillLine = new BillLineDto(1L, "Pizza", new BigDecimal("1000"), 1, new BigDecimal("500"));
        when(orderMapper.orderItemToBillLineShared(eq(sharedItem), any(BigDecimal.class)))
            .thenReturn(sharedBillLine);

        BillResponse response = billService.splitBills(1L, waiter);

        assertThat(response).isNotNull();
        assertThat(response.bills()).hasSize(2);
        assertThat(response.bills()).allMatch(bill -> bill.details().size() == 1);
        verify(orderMapper, times(1)).orderItemToBillLineShared(eq(sharedItem), any(BigDecimal.class));
        verify(orderMapper, never()).orderItemToBillLinePrivate(any());
        verify(guestRepository).findAllByOrderId(1L);
    }

    // test 4
    @Test
    void testSplitBills_OnlyPrivateItems() {
        List<OrderItemEntity> orderItems = List.of(privateItem1, privateItem2);

        when(orderHelperService.getInstance(1L, 1L)).thenReturn(order);
        when(guestRepository.countByOrderId(1L)).thenReturn(2);
        when(orderItemRepository.findAllByOrderId(1L)).thenReturn(orderItems);
        when(orderHelperService.calculateTotalPrice(orderItems)).thenReturn(new BigDecimal("1500"));
        when(commissionService.getMerchantCommissionRate(1L)).thenReturn(new BigDecimal("0.10"));

        BillLineDto privateBillLine1 = new BillLineDto(2L, "Burger", new BigDecimal("500"), 2, new BigDecimal("1000"));
        when(orderMapper.orderItemToBillLinePrivate(privateItem1)).thenReturn(privateBillLine1);

        BillLineDto privateBillLine2 = new BillLineDto(2L, "Burger", new BigDecimal("500"), 1, new BigDecimal("500"));
        when(orderMapper.orderItemToBillLinePrivate(privateItem2)).thenReturn(privateBillLine2);

        BillResponse response = billService.splitBills(1L, waiter);

        assertNotNull(response);
        assertEquals(2, response.bills().size());
        verify(orderMapper, never()).orderItemToBillLineShared(any(), any());
        verify(orderMapper, times(2)).orderItemToBillLinePrivate(any());
    }
}