package com.example.outsourcing_taskflow.domain.user.service;

import com.example.outsourcing_taskflow.common.annotaion.MeasureAllMethods;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.common.security.JwtUtil;
import com.example.outsourcing_taskflow.domain.member.repository.MemberRepository;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserDto;
import com.example.outsourcing_taskflow.domain.user.model.request.CreateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.LoginUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.UpdateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.VerifyPasswordRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.*;
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
@MeasureAllMethods
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     */
    public CreateUserResponse createUser(CreateUserRequest request) {

        // IsDeleted.TRUE 회원가입 불가
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new IllegalArgumentException("탈퇴한 회원 - 재가입 불가");
        }

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

        // 탈퇴한 회원은 로그인 불가
        if (user.getIsDeleted().equals(IsDeleted.TRUE)) {
            throw new IllegalArgumentException("탈퇴한 회원 - 로그인 불가");
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

        // 탈퇴한 회원은 정보 조회 불가
        if (user.getIsDeleted().equals(IsDeleted.TRUE)) {
            throw new IllegalArgumentException("탈퇴한 회원 - 사용자 정보 조회 불가");
        }

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

            UserDto userDto = UserDto.from(user);

            // 탈퇴.FALSE인 유저만 리스트에 추가
            if (user.getIsDeleted().equals(IsDeleted.FALSE)) {
                GetAllResponse response = new GetAllResponse(
                        userDto.getId(),
                        userDto.getUserName(),
                        userDto.getEmail(),
                        userDto.getName(),
                        userDto.getRole(),
                        userDto.getCreatedAt()
                );

                responseList.add(response);
            }
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

        User user = userRepository.findById(authUserId).orElseThrow(
                () -> new CustomException(ErrorMessage.NOT_FOUND_USER)
        );

        // 탈퇴한 회원은 재탈퇴 불가
        if (user.getIsDeleted().equals(IsDeleted.TRUE)) {
            throw new IllegalArgumentException("이미 탈퇴한 회원");
        }

        user.softDelete(IsDeleted.TRUE);
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


    /**
     * 추가 가능한 사용자 조회
     */
    public List<AvailableUserResponse> availableUser(Long teamId) {

        // teamId가 존재할 때, 해당 팀에 속한 멤버를 제외한 사용자를 불러와라
        // 팀에 없는 사용자 리스트 반환
        List<User> userList = memberRepository.findUsersNotInTeam(teamId);
        List<AvailableUserResponse> responseList = new ArrayList<>();

        for (User user: userList) {

            // 탈퇴한 사용자 제외
            if (user.getIsDeleted().equals(IsDeleted.FALSE)) {

                UserDto userDto = UserDto.from(user);

                AvailableUserResponse response = new AvailableUserResponse(
                        userDto.getId(),
                        userDto.getUserName(),
                        userDto.getEmail(),
                        userDto.getName(),
                        userDto.getRole(),
                        userDto.getCreatedAt()
                );

                responseList.add(response);
            }
        }

        return responseList;
    }
}
