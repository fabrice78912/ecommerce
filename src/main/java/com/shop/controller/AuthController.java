package com.shop.controller;


import com.shop.config.JwtProvider;
import com.shop.exception.UserException;
import com.shop.model.User;
import com.shop.repo.UserRepository;
import com.shop.request.LoginRequest;
import com.shop.response.AuthResponse;
import com.shop.service.impl.CustomUserServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.BadCredentialsException;
import org.example.common.exception.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserServiceImpl customUserServiceImpl;


    @PostMapping("/signup")
    //@Hidden
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new UserExistException("Email "+ email +" is already used with another account");
        }
        User createdUser = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        log.info("Authentication {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(token)
                .message("Signup success")
                .build();

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }


    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) throws UserException {

        String username= loginRequest.getEmail();
        String password= loginRequest.getPassword();

        Authentication authentication= authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = AuthResponse.builder()
                .jwt(token)
                .message("signing success")
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails= customUserServiceImpl.loadUserByUsername(username);
        if(userDetails== null){
            log.info("Invalid username {}", userDetails.getUsername());
            throw new BadCredentialsException("Invalid username..");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            log.info("Invalid password {}", userDetails.getPassword());
            throw new BadCredentialsException("Invalid password..");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities() );
    }
}
