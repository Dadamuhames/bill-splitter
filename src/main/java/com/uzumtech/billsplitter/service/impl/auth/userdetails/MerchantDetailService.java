package com.uzumtech.billsplitter.service.impl.auth.userdetails;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.exception.LoginNotFoundException;
import com.uzumtech.billsplitter.repository.MerchantRepository;
import com.uzumtech.billsplitter.service.auth.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MerchantDetailService implements CustomUserDetailsService {
    private final MerchantRepository merchantRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) throws LoginNotFoundException {
        return merchantRepository.findByLogin(login).orElseThrow(
            () -> new LoginNotFoundException(Error.LOGIN_INVALID_CODE)
        );
    }

    @Override
    public Role getSupportedRole() {
        return Role.MERCHANT;
    }
}
