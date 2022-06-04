package ru.netology.cloud_storage_backend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.netology.cloud_storage_backend.dto.AuthenticationDTO;
import ru.netology.cloud_storage_backend.model.token.TokenBlacklist;
import ru.netology.cloud_storage_backend.repository.TokenBlacklistRepository;
import ru.netology.cloud_storage_backend.security.jwt.JwtTokenProvider;
import ru.netology.cloud_storage_backend.service.AuthenticationService;
import ru.netology.cloud_storage_backend.service.UserService;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public String getToken(AuthenticationDTO authentication) {
        try {
            var login = authentication.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, authentication.getPassword()));
            var user = userService.findByEmail(login);
            return jwtTokenProvider.createToken(login, user.getRoles());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public void removeToken(String token) {
        var forbiddenToken = new TokenBlacklist();
        forbiddenToken.setToken(token);
        tokenBlacklistRepository.save(forbiddenToken);
    }
}
