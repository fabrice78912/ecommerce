package com.shop.service.impl;

import com.shop.config.JwtProvider;
import com.shop.exception.UserException;
import com.shop.model.User;
import com.shop.repo.UserRepository;
import com.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.example.common.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> optionalUser= userRepository.findById(userId);

        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UserNotFoundException("User not found with "+ userId);
    }

    @Override
    public User findUserByJwt(String jwt) throws UserException {
        String email= jwtProvider.getEmailFromToken(jwt);
        User user= userRepository.findByEmail(email);
        if(user== null){
            throw  new UserNotFoundException("User not found with email "+ email);
        }
        return user;
    }
}
