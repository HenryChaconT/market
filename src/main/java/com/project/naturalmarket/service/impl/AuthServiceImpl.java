package com.project.naturalmarket.service.impl;

import com.project.naturalmarket.dto.LoginDto;
import com.project.naturalmarket.dto.RegisterDto;
import com.project.naturalmarket.entity.Role;
import com.project.naturalmarket.entity.User;
import com.project.naturalmarket.entity.roleEnum.ERole;
import com.project.naturalmarket.exception.MarketAPIException;
import com.project.naturalmarket.exception.ResourceNotFoundException;
import com.project.naturalmarket.repository.RoleRepository;
import com.project.naturalmarket.repository.UserRepository;
import com.project.naturalmarket.security.JwtTokenProvider;
import com.project.naturalmarket.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtTokenProvider=jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(),loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new MarketAPIException(HttpStatus.BAD_REQUEST, "El usuario ya esxiste");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new MarketAPIException(HttpStatus.BAD_REQUEST, "El email ya esxiste");
        }

        //Set<Role> roles=registerDto.getRole().stream().map(role-> Role.builder().name(ERole.valueOf(role)).build()).collect(Collectors.toSet());

        //Set<Role> roles=roleRepository.findByName(registerDto.getRole()).stream().collect(Collectors.toSet());

        Set<Role> roles=roleRepository.findAllByName(registerDto.getRole());
        if (roles == null || roles.isEmpty() || roles.size()!=registerDto.getRole().size()){
            throw new ResourceNotFoundException("Roles","asignado",1);
        }

        User user =new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setAddress(registerDto.getAddress());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);

        return "El usuario ha sido registrado correctamente";
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();

    }
}
