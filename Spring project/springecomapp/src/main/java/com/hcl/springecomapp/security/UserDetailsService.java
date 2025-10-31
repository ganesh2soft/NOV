package com.hcl.springecomapp.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    
    /**
     * Locates the user based on the username.
     * @param username the username identifying the user whose data is required.
     * @return a fully populated UserDetails object (never null)
     * @throws UsernameNotFoundException if the user could not be found
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
