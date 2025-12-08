package com.uzumtech.billsplitter.service.auth.userdetails;

import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.exception.LoginNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsServiceDispatcher {
    UserDetails loadUserByLoginAndRole(String login, Role role) throws LoginNotFoundException;
}
