package com.uzumtech.billsplitter.service.impl.auth;


import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.dto.request.auth.LoginRequest;
import com.uzumtech.billsplitter.dto.request.auth.MerchantRegistrationRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import com.uzumtech.billsplitter.exception.MerchantValidationException;
import com.uzumtech.billsplitter.exception.PasswordInvalidException;
import com.uzumtech.billsplitter.mapper.MerchantMapper;
import com.uzumtech.billsplitter.repository.MerchantRepository;
import com.uzumtech.billsplitter.service.auth.MerchantAuthService;
import com.uzumtech.billsplitter.service.auth.token.TokenService;
import com.uzumtech.billsplitter.service.impl.auth.userdetails.MerchantDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantAuthServiceImpl implements MerchantAuthService {
    private final MerchantRepository merchantRepository;
    private final MerchantDetailService merchantDetailService;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public TokenResponse register(final MerchantRegistrationRequest request) {
        validateRegisterRequest(request);

        MerchantEntity merchant = merchantMapper.requestToEntity(request);

        merchant.setPassword(passwordEncoder.encode(request.password()));

        merchantRepository.save(merchant);

        return tokenService.createPair(merchant);
    }

    private void validateRegisterRequest(final MerchantRegistrationRequest request) {
        boolean loginOrTaxNumberExists = merchantRepository.existsByLoginOrTaxNumber(request.login(), request.taxNumber());

        if (loginOrTaxNumberExists) {
            throw new MerchantValidationException(Error.LOGIN_OR_TAX_NUMBER_EXISTS_CODE);
        }
    }

    @Override
    public TokenResponse login(final LoginRequest request) {
        MerchantEntity merchant = (MerchantEntity) merchantDetailService.loadUserByUsername(request.login());

        if (!passwordEncoder.matches(request.password(), merchant.getPassword())) {
            throw new PasswordInvalidException(Error.PASSWORD_INVALID_CODE);
        }

        return tokenService.createPair(merchant);
    }
}
