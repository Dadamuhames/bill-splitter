package com.uzumtech.billsplitter.service.impl.order;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.dto.request.order.OrderCreateRequest;
import com.uzumtech.billsplitter.dto.request.order.OrderMealDto;
import com.uzumtech.billsplitter.entity.MealEntity;
import com.uzumtech.billsplitter.exception.MealInvalidException;
import com.uzumtech.billsplitter.exception.MealNotFoundException;
import com.uzumtech.billsplitter.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderMealHelperService {
    private final MealRepository mealRepository;

    public MealEntity findByIdAndMerchantId(final Long id, final Long merchantId) {
        return mealRepository.findByIdAndMerchantId(id, merchantId).orElseThrow(
            () -> new MealNotFoundException(Error.MEAL_NOT_FOUND_CODE)
        );
    }

    public Map<Long, MealEntity> fetchRelevantMeals(final OrderCreateRequest request, final Long merchantId) {
        Set<Long> mealIds = new HashSet<>();

        for (var guest : request.guests()) {
            mealIds.addAll(guest.meals().stream().map(OrderMealDto::mealId).toList());
        }

        if (request.sharedMeals() != null) {
            mealIds.addAll(request.sharedMeals().stream().map(OrderMealDto::mealId).toList());
        }

        List<MealEntity> meals = mealRepository.findAllByMerchantIdAndIdIn(merchantId, mealIds);

        if (meals.size() != mealIds.size()) {
            throw new MealInvalidException(Error.INVALID_MEAL_ID_PROVIDED_CODE);
        }

        return meals.stream().collect(Collectors.toMap(MealEntity::getId, m -> m));
    }

    public Map<Long, MealEntity> fetchRelevantMeals(final List<OrderMealDto> orderMeals, final Long merchantId) {
        Set<Long> mealIds = new HashSet<>(orderMeals.stream().map(OrderMealDto::mealId).toList());

        List<MealEntity> meals = mealRepository.findAllByMerchantIdAndIdIn(merchantId, mealIds);

        if (meals.size() != mealIds.size()) {
            throw new MealInvalidException(Error.INVALID_MEAL_ID_PROVIDED_CODE);
        }

        return meals.stream().collect(Collectors.toMap(MealEntity::getId, m -> m));
    }
}
