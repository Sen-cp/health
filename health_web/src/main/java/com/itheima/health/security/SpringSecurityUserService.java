package com.itheima.health.security;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {


    @Reference
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.itheima.health.pojo.User userInDB = userService.findByUsername(username);


        if (null != userInDB) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            Set<Role> roles = userInDB.getRoles();
            SimpleGrantedAuthority grantedAuthority = null;
            if (null != roles) {
                for (Role role : roles) {
                    String keyword = role.getKeyword();
                    grantedAuthority = new SimpleGrantedAuthority(keyword);
                    authorities.add(grantedAuthority);

                    Set<Permission> permissions = role.getPermissions();
                    if (null != permissions) {
                        for (Permission permission : permissions) {
                            String permissionKeyword = permission.getKeyword();
                            grantedAuthority = new SimpleGrantedAuthority(permissionKeyword);
                            authorities.add(grantedAuthority);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(userInDB.getUsername(), userInDB.getPassword(), authorities);
        }
        return null;
    }
}
