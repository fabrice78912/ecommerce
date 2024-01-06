package com.shop.service;

import com.shop.exception.UserException;
import com.shop.model.User;


public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserByJwt(String jwt) throws UserException;

}
