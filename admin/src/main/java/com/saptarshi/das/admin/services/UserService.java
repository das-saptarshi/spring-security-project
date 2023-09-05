package com.saptarshi.das.admin.services;

import com.saptarshi.das.admin.models.UserEntity;
import com.saptarshi.das.admin.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }
}
