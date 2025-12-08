package com.uzumtech.billsplitter.service.impl.auth.userdetails;


import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.exception.LoginNotFoundException;
import com.uzumtech.billsplitter.repository.WaiterRepository;
import com.uzumtech.billsplitter.service.auth.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaiterDetailService implements CustomUserDetailsService {
    private final WaiterRepository waiterRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws LoginNotFoundException {
        return waiterRepository.findByLogin(login).orElseThrow(
            () -> new LoginNotFoundException(Error.LOGIN_INVALID_CODE)
        );
    }

    @Override
    public Role getSupportedRole() {
        return Role.WAITER;
    }
}
