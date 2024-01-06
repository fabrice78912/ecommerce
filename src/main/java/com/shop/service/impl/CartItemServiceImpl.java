package com.shop.service.impl;

import com.shop.exception.UserException;
import com.shop.model.Cart;
import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repo.CartItemRepository;
import com.shop.repo.CartRepository;
import com.shop.service.CartItemService;
import com.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.example.common.exception.CartItemException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemrepository;
    private final UserService userService;
    private final CartRepository cartRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
        CartItem createdCartItem = cartItemrepository.save(cartItem);
        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userid, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getUserId());

        if (user.getId().equals(userid)) {
            item.setQuantity(item.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
        }
        return cartItemrepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {

        CartItem cartItem= cartItemrepository.isCartItemExist(cart,product,size,userId);
        return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem= findCartItemById(cartItemId);
        User user= userService.findUserById(cartItem.getUserId());
        User reqUser= userService.findUserById(userId);

        if(user.getId().equals(reqUser.getId())){
            cartItemrepository.deleteById(cartItemId);
        }else {
            throw new UserException("You can't remove another users item");
        }

    }

    @Override
    public CartItem findCartItemById(Long cartItem) throws CartItemException, UserException {
        Optional<CartItem> opt= cartItemrepository.findById(cartItem);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new CartItemException("Cart Item not found with id :"+cartItem);
    }
}
