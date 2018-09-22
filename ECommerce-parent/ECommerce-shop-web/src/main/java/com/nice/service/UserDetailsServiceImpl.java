package com.nice.service;

import com.nice.pojo.TbSeller;
import com.nice.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录认证业务层
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbSeller seller = sellerService.findOne(username);
        if (seller!=null) {
            User user = new User(username, seller.getPassword(),
                    seller.getStatus().equals("1"),
                    true,true,true,
                    getauthorities(seller));
            return user;
        }
        return null;
    }

    /**
     * 获取权限
     * @param seller
     * @return
     */
    public List<GrantedAuthority> getauthorities(TbSeller seller) {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        return list;
    }
}
