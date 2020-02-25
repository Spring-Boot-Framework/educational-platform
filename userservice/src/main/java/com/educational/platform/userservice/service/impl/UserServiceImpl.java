package com.educational.platform.userservice.service.impl;

import com.educational.platform.common.exception.UnprocessableEntityException;
import com.educational.platform.userservice.mapper.UserMapper;
import com.educational.platform.userservice.model.User;
import com.educational.platform.userservice.model.dto.UserRegistrationDTO;
import com.educational.platform.userservice.repository.UserRepository;
import com.educational.platform.userservice.security.JwtTokenProvider;
import com.educational.platform.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper;

    @Override
    public String signIn(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }

    @Override
    public String signUp(UserRegistrationDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UnprocessableEntityException(String.format("Username: [%s] is already in use", dto.getUsername()));
        }

        final User user = mapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return jwtTokenProvider.createToken(dto.getUsername(), dto.getRoles());
    }

}
