package com.uzumtech.billsplitter.service.impl.auth.userdetails;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.exception.LoginNotFoundException;
import com.uzumtech.billsplitter.exception.RoleNotSupportedException;
import com.uzumtech.billsplitter.service.auth.userdetails.CustomUserDetailsService;
import com.uzumtech.billsplitter.service.auth.userdetails.UserDetailsServiceDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceDispatcherImpl implements UserDetailsServiceDispatcher {
    private final Map<Role, CustomUserDetailsService> customUserDetailServices;

    public UserDetails loadUserByLoginAndRole(String login, Role role) throws LoginNotFoundException {
        CustomUserDetailsService userDetailsService = customUserDetailServices.get(role);

        if (userDetailsService == null) {
            throw new RoleNotSupportedException(Error.ROLE_NOT_SUPPORTED_CODE);
        }

        return userDetailsService.loadUserByUsername(login);
    }
}
