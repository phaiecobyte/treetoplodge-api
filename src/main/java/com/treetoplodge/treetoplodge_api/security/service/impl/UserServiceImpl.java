package com.treetoplodge.treetoplodge_api.security.service.impl;


import com.treetoplodge.treetoplodge_api.repository.UserRepository;
import com.treetoplodge.treetoplodge_api.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsService userdetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
                return userRepository.findByPhoneNumber(phone)
                        .orElseThrow(()->new UsernameNotFoundException("User not found"));
            }
        };
    }
}
