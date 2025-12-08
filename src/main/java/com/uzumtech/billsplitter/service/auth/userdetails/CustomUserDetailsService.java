package com.uzumtech.billsplitter.service.auth.userdetails;

import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.exception.LoginNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(final String login) throws LoginNotFoundException;

    Role getSupportedRole();
}
