package com.gp.barter.exchange.security;


import com.gp.barter.exchange.persistence.model.UserData;
import com.gp.barter.exchange.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserData> user = userService.getUserByEmail(s);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("No user found with email " + s);
        }
        return new AccountUserDetails(user.get());
    }
}
