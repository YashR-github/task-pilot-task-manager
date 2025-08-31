package org.example.taskpilot_taskmanager.user.service;


import jakarta.transaction.Transactional;
import org.example.taskpilot_taskmanager.notifications.dispatch.UnifiedNotificationDispatcher;
import org.example.taskpilot_taskmanager.notifications.dtos.EmailRequestDTO;
import org.example.taskpilot_taskmanager.user.dtos.AuthLoginResponseDTO;
import org.example.taskpilot_taskmanager.user.dtos.UserLoginResponseDTO;
import org.example.taskpilot_taskmanager.user.exceptions.InvalidCredentialsException;
import org.example.taskpilot_taskmanager.user.exceptions.UserNotFoundException;
import org.example.taskpilot_taskmanager.user.util.UserEntityDtoMapper;
import org.example.taskpilot_taskmanager.user.dtos.UserSignupResponseDTO;
import org.example.taskpilot_taskmanager.user.enums.UserRole;
import org.example.taskpilot_taskmanager.user.enums.UserStatus;
import org.example.taskpilot_taskmanager.user.exceptions.UserAlreadyExistException;

import org.example.taskpilot_taskmanager.user.model.User;
import org.example.taskpilot_taskmanager.user.repositories.UserRepository;
import org.example.taskpilot_taskmanager.user.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // interface for bCrypt
    private final JwtUtil jwtUtil;
    private final UnifiedNotificationDispatcher unifiedNotificationDispatcher;


    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UnifiedNotificationDispatcher unifiedNotificationDispatcher ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.unifiedNotificationDispatcher = unifiedNotificationDispatcher;
    }


    @Transactional
    public UserSignupResponseDTO signUp(String name, String username, String email, String password) throws UserAlreadyExistException {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException("User with username already exist");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException("User with email already exist.Please Login instead or choose different Email ID");
        }
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); //hash before setting
        user.setUserRole(UserRole.USER);
        user.setUserStatus(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(user.getEmail());
        emailRequestDTO.setSubject("Sign up successful !");
        emailRequestDTO.setContent("Hi "+user.getName()+ " , Welcome to TaskPilot ! Your account has been created successfully. Please login to continue.");
        unifiedNotificationDispatcher.dispatch(emailRequestDTO);

        return UserEntityDtoMapper.toUserSignupResponseDto(savedUser);

    }



    @Transactional
    public AuthLoginResponseDTO login(String username, String email, String password) throws UserNotFoundException {
        Optional<User> optionalUser= Optional.empty();

        if(username!=null && !username.isBlank()) {
            optionalUser= userRepository.findByUsername(username);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException("User with username not found");
            }
        }
        else if(email!=null && !email.isBlank()) {
            optionalUser= userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException("User with email not found");
            }
        }
        else { // case already handled in controller
            throw new InvalidCredentialsException("Username or Email must be provided");
        }

        User user = optionalUser.get();

        //check password match
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username/email or password");
        }


        //generating jwt token
        String jwtToken = jwtUtil.generateToken(user.getUsername(), user.getUserRole().name());
        UserLoginResponseDTO userLoginResponseDto = UserEntityDtoMapper.toUserLoginResponseDto(user);

        AuthLoginResponseDTO authLoginResponseDto = new AuthLoginResponseDTO(jwtToken, userLoginResponseDto);

        return authLoginResponseDto;

    }


}