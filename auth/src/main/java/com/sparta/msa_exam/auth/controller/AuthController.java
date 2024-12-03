package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.dto.LoginRequestDto;
import com.sparta.msa_exam.auth.dto.SignUpRequestDto;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.repository.UserRepository;
import com.sparta.msa_exam.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto requestDto) {
        // username 중복 체크
        if (userRepository.findByUsername(requestDto.getUsername()) != null) {
            return ResponseEntity.status(409).body("Username already exists");
        }

        // 비밀번호 암호화 및 저장
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), encodedPassword);
        userRepository.save(user); // Redis에 데이터 저장

        return ResponseEntity.ok("Sign-up successful");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        // JWT 생성 및 반환
        String token = jwtUtil.createToken(user.getUsername());
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).body("Login successful");
    }
}
