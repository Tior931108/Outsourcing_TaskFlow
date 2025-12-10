package com.example.outsourcing_taskflow.domain.user.service;

import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserDto;
import com.example.outsourcing_taskflow.domain.user.model.request.CreateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.CreateUserResponse;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


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

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword(), request.getName());

        userRepository.save(user);

        UserDto userDto = UserDto.from(user);

        return CreateUserResponse.from(userDto);
    }
}
