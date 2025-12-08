package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.constant.enums.OrderStatus;
import com.uzumtech.billsplitter.entity.TableEntity;
import com.uzumtech.billsplitter.entity.order.GuestEntity;
import com.uzumtech.billsplitter.entity.order.OrderEntity;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
public class GuestRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WaiterRepository waiterRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    TestEntityManager entityManager;

    private OrderEntity order1;
    private OrderEntity order2;
    private GuestEntity guest1;
    private GuestEntity guest2;

    @BeforeEach
    public void setUp() {
        MerchantEntity merchant = MerchantEntity.builder()
            .login("merch")
            .password("PASS")
            .taxNumber("123456789")
            .name("name")
            .build();

        merchant = merchantRepository.save(merchant);

        WaiterEntity waiter = WaiterEntity.builder()
            .fullName("John Waiter")
            .login("login")
            .password("SOMEPASS")
            .merchant(merchant)
            .build();
        waiter = waiterRepository.save(waiter);

        TableEntity table = TableEntity.builder()
            .tableNumber("1")
            .merchant(merchant)
            .build();
        table = tableRepository.save(table);

        order1 = OrderEntity.builder()
            .waiter(waiter)
            .table(table)
            .status(OrderStatus.OPEN)
            .build();
        order1 = orderRepository.save(order1);

        order2 = OrderEntity.builder()
            .waiter(waiter)
            .table(table)
            .status(OrderStatus.OPEN)
            .build();
        order2 = orderRepository.save(order2);

        guest1 = GuestEntity.builder()
            .order(order1)
            .build();

        guest2 = GuestEntity.builder()
            .order(order1)
            .build();
    }

    @Test
    void testFindByIdAndOrderId_Success() {
        GuestEntity saved = guestRepository.save(guest1);
        entityManager.flush();
        entityManager.clear();

        Optional<GuestEntity> found = guestRepository.findByIdAndOrderId(saved.getId(), order1.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(order1.getId(), found.get().getOrder().getId());
    }

    @Test
    void testFindByIdAndOrderId_WrongOrderId() {
        GuestEntity saved = guestRepository.save(guest1);
        entityManager.flush();
        entityManager.clear();

        Optional<GuestEntity> found = guestRepository.findByIdAndOrderId(saved.getId(), order2.getId());

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByIdAndOrderId_WrongGuestId() {
        guestRepository.save(guest1);
        entityManager.flush();
        entityManager.clear();

        Optional<GuestEntity> found = guestRepository.findByIdAndOrderId(9999L, order1.getId());

        assertFalse(found.isPresent());
    }

    @Test
    void testCountByOrderId_MultipleGuests() {
        guestRepository.saveAll(List.of(guest1, guest2));
        entityManager.flush();
        entityManager.clear();

        int count = guestRepository.countByOrderId(order1.getId());

        assertEquals(2, count);
    }

    @Test
    void testCountByOrderId_NoGuests() {
        int count = guestRepository.countByOrderId(order1.getId());

        assertEquals(0, count);
    }

    @Test
    void testCountByOrderId_DifferentOrders() {
        GuestEntity guest3 = GuestEntity.builder()
            .order(order2)
            .build();

        guestRepository.saveAll(List.of(guest1, guest2, guest3));
        entityManager.flush();
        entityManager.clear();

        int countOrder1 = guestRepository.countByOrderId(order1.getId());
        int countOrder2 = guestRepository.countByOrderId(order2.getId());

        assertEquals(2, countOrder1);
        assertEquals(1, countOrder2);
    }

    @Test
    void testFindAllByOrderId_Success() {
        guestRepository.saveAll(List.of(guest1, guest2));
        entityManager.flush();
        entityManager.clear();

        List<GuestEntity> guests = guestRepository.findAllByOrderId(order1.getId());

        assertEquals(2, guests.size());
        assertTrue(guests.stream().allMatch(g -> g.getOrder().getId().equals(order1.getId())));
    }

    @Test
    void testFindAllByOrderId_EmptyList() {
        List<GuestEntity> guests = guestRepository.findAllByOrderId(order1.getId());

        assertTrue(guests.isEmpty());
    }

    @Test
    void testFindAllByOrderId_FiltersByOrder() {
        GuestEntity guest3 = GuestEntity.builder()
            .order(order2)
            .build();

        guestRepository.saveAll(List.of(guest1, guest2, guest3));
        entityManager.flush();
        entityManager.clear();

        List<GuestEntity> guestsOrder1 = guestRepository.findAllByOrderId(order1.getId());
        List<GuestEntity> guestsOrder2 = guestRepository.findAllByOrderId(order2.getId());

        assertEquals(2, guestsOrder1.size());
        assertEquals(1, guestsOrder2.size());
        assertTrue(guestsOrder1.stream().allMatch(g -> g.getOrder().getId().equals(order1.getId())));
        assertTrue(guestsOrder2.stream().allMatch(g -> g.getOrder().getId().equals(order2.getId())));
    }
}