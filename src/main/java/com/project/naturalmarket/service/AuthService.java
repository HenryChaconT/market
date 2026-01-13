package com.project.naturalmarket.service;

import com.project.naturalmarket.dto.LoginDto;
import com.project.naturalmarket.dto.RegisterDto;

public interface AuthService {

    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    void logout();
}
