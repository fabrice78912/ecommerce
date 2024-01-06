package com.shop.config;

import com.shop.model.User;
import com.shop.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserInit {

    private final UserRepository userDao;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void postConstruct() {

        String email = "admin@gmail.com";
        User user = userDao.findByEmail(email);
        if (user == null) {
            userDao.save(User.builder()
                    .email(email)
                    .firstName("admin")
                    .lastName("admin")
                    .password(passwordEncoder.encode("password"))
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }
}
