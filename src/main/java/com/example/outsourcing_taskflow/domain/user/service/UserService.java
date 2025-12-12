package com.example.outsourcing_taskflow.domain.user.service;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.common.config.security.JwtUtil;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserDto;
import com.example.outsourcing_taskflow.domain.user.model.request.CreateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.LoginUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.UpdateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.VerifyPasswordRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.CreateUserResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.GetAllResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.GetUserResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.UpdateUserResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.VerifyPasswordResponse;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public GetUserResponse getUser(Long id, Long authUserId) {

        // 사용자를 찾을 수 없을 때
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_USER)
        );

        // 본인 id가 아닐 때
        if (!id.equals(authUserId)) {
            throw new CustomException(ErrorMessage.NEED_TO_VALID_TOKEN);
        }

        UserDto userDto = UserDto.from(user);

        return GetUserResponse.from(userDto);
    }


    /**
     * 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<GetAllResponse> getAll() {

        List<User> userList = userRepository.findAll();
        List<GetAllResponse> responseList = new ArrayList<>();

        for (User user: userList) {
            GetAllResponse response = new GetAllResponse(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    user.getCreatedAt()
            );

            responseList.add(response);
        }

        return responseList;
    }


    /**
     * 사용자 정보 수정
     */
    public UpdateUserResponse updateUser(Long id, UpdateUserRequest request, Long authUserID) {

        User user = userRepository.findById(authUserID).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_USER)
        );

        // 다른 사용자 정보 수정 시도
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorMessage.ONLY_OWNER_ACCESS);
        }

        // 중복된 이메일
        if (user.getEmail().equals(request.getEmail())) {
            throw new CustomException(ErrorMessage.EXIST_EMAIL);
        }

        user.update(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));

        UserDto userDto = UserDto.from(user);

        return UpdateUserResponse.from(userDto);
    }


    /**
     * 회원 탈퇴
     */
    public void deleteUser(Long id, Long authUserId) {

        // 다른 사용자 탈퇴 시도
        if (!id.equals(authUserId)) {
            throw new CustomException(ErrorMessage.ONLY_OWNER_ACCESS);
        }

        userRepository.deleteById(authUserId);
    }


    /**
     * 비밀번호 확인
     */
    public VerifyPasswordResponse verifyPassword(VerifyPasswordRequest request, Long authUserId) {

        User user = userRepository.findById(authUserId).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_USER)
        );


        VerifyPasswordResponse verifyPasswordResponse = new VerifyPasswordResponse();

        // 잘못된 비밀번호
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            verifyPasswordResponse.setValid(false);
        } else {
            verifyPasswordResponse.setValid(true);
        }


        return verifyPasswordResponse;
    }
}
