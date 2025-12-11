package com.example.outsourcing_taskflow.domain.user.service;

import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.common.utils.JwtUtil;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserDto;
import com.example.outsourcing_taskflow.domain.user.model.request.CreateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.LoginUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.CreateUserResponse;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     * @param request
     */
    public CreateUserResponse createUser(CreateUserRequest request) {

        // 중복된 사용자명 예외 처리
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new CustomException(ErrorMessage.EXIST_NAME);
        }

        // 중복된 이메일 예외 처리
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorMessage.EXIST_NAME);
        }

        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName());

        userRepository.save(user);

        UserDto userDto = UserDto.from(user);

        return CreateUserResponse.from(userDto);
    }

    /**
     * 로그인
     */
    public String login(LoginUserRequest request) {

        String username = request.getUsername();
        String password = request.getPassword();

        // 잘못된 인증 정보 시, 아이디 또는 비밀번호가 올바르지 않는 예외 처리
        User user = userRepository.findByUserName(username).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_MATCH_UNAUTHORIZED)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorMessage.NOT_MATCH_UNAUTHORIZED);
        }

        return jwtUtil.generateToken(user.getId(), user.getUserName(), user.getRole());

    }
}
