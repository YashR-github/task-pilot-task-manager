package org.example.taskpilot_taskmanager.user.controller;


import jakarta.validation.Valid;
import org.example.taskpilot_taskmanager.common.dtos.ResponseDTO;
import org.example.taskpilot_taskmanager.user.dtos.*;
import org.example.taskpilot_taskmanager.user.exceptions.InvalidCredentialsException;
import org.example.taskpilot_taskmanager.user.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService)
    {    this.authService = authService;
    }


    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<UserSignupResponseDTO>> signup(@RequestBody @Valid UserSignupRequestDTO request){
UserSignupResponseDTO userResponseDto=authService.signUp(request.getName(), request.getUsername(), request.getEmail(), request.getPassword());
            ResponseDTO<UserSignupResponseDTO> responseDto=new ResponseDTO<>("User Signed Up Successfully!",userResponseDto);
            return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<UserLoginResponseDTO>> login(@RequestBody @Valid UserLoginRequestDTO request){
        String username = request.getUsername();
        String email = request.getEmail();

        if((username==null || username.isBlank()) && (email==null || email.isBlank())){
            throw new InvalidCredentialsException("Username or Email must be provided");
        }

        AuthLoginResponseDTO authLoginResponseDto = authService.login(username, email, request.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,"Bearer "+authLoginResponseDto.getToken());


        ResponseDTO<UserLoginResponseDTO> responseDto = new ResponseDTO<>("User Logged In Successfully!", authLoginResponseDto.getUserLoginResponseDto());
        return new ResponseEntity<>(responseDto, headers, HttpStatus.OK);
    }

}
