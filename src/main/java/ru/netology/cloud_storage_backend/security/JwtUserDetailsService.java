package ru.netology.cloud_storage_backend.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloud_storage_backend.security.jwt.JwtUserFactory;
import ru.netology.cloud_storage_backend.service.UserService;

@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findByEmail(username);
        var jwtUser = JwtUserFactory.create(user);
        log.info("User with username: {} successfully loaded", username);
        return jwtUser;
    }
}
