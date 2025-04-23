package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import com.wait.app.domain.dto.cart.CartListDTO;
import com.wait.app.domain.entity.Cart;
import com.wait.app.domain.param.cart.CartSaveParam;
import com.wait.app.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 天
 * Time: 2025/4/23 13:29
 */
@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String cartId) {
        cartRepository.removeById(cartId);
    }


    @Transactional(rollbackFor = Exception.class)
    public void save(CartSaveParam cartSaveParam,String userId) {
        Cart cart = BeanUtil.copyProperties(cartSaveParam, Cart.class);
        cart.setUserId(userId);
        cartRepository.saveOrUpdate(cart);
    }

    public List<CartListDTO> list(String userId) {
        List<Cart> list = cartRepository.lambdaQuery().eq(Cart::getUserId, userId).list();
        return BeanUtil.copyToList(list, CartListDTO.class);
    }
}
