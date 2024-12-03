package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.dto.LoginRequestDto;
import com.sparta.msa_exam.auth.dto.SignUpRequestDto;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.repository.UserRepository;
import com.sparta.msa_exam.auth.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    // 8. 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto requestDto, HttpServletRequest request) {
        // username 중복 체크
        if (userRepository.findByUsername(requestDto.getUsername()) != null) {
            return ResponseEntity.status(409)
                    .header("Server-Port", String.valueOf(request.getLocalPort())) // Server-Port 헤더 추가
                    .body("이미 존재하는 username입니다.");
        }

        // 비밀번호 암호화 및 저장
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), encodedPassword);
        userRepository.save(user);

        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(request.getLocalPort())) // Server-Port 헤더 추가
                .body("회원가입이 완료되었습니다.");
    }

    // 7. 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody LoginRequestDto requestDto, HttpServletRequest request) {
        User user = userRepository.findByUsername(requestDto.getUsername());
        if (user == null) {
            return ResponseEntity.status(401)
                    .header("Server-Port", String.valueOf(request.getLocalPort())) // Server-Port 헤더 추가
                    .body("찾을 수 없는 유저입니다.");
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                    .header("Server-Port", String.valueOf(request.getLocalPort())) // Server-Port 헤더 추가
                    .body("Invalid username or password");
        }

        // JWT 생성 및 반환
        String token = jwtUtil.createToken(user.getUsername());
        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(request.getLocalPort())) // Server-Port 헤더 추가
                .header("Authorization", "Bearer " + token)
                .body("로그인 성공");
    }
}
