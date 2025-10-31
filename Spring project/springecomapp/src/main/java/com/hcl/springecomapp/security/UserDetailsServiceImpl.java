package com.hcl.springecomapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.springecomapp.entity.Users;
import com.hcl.springecomapp.repository.UsersRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsersRepository usersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Users user = usersRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    	System.out.println("email received at service "+email);
    	Users user = usersRepository.findByEmail(email)
    	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with emailiid: " + email));
        return UserDetailsImpl.build(user);
    }


}