package product.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.config.jwt.TokenProvider;
import product.dto.user.*;
import product.entity.user.Authority;
import product.entity.user.User;
import product.exception.ExceptionType;
import product.exception.RequestException;
import product.repository.user.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthRedisService authRedisService;




    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail()))
            throw new RequestException(ExceptionType.ALREADY_EXISTS_EXCEPTION);

        userRepository.save(User.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .age(signUpRequestDto.getAge())
                .authority(Authority.ROLE_USER)
                .build());
    }


    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RequestException(ExceptionType.NOT_FOUND_USER_EXCEPTION));

        validatePassword(loginRequestDto, user);

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        authRedisService.setValues(authentication.getName(), tokenDto.getRefreshToken(), Duration.ofDays(1));

        response.addHeader("AccessToken", tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());

        // 5. 토큰 발급
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto, HttpServletResponse response) {

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        authRedisService.checkRefreshToken(authentication.getName(), tokenRequestDto.getRefreshToken());

        // 예외 처리 통과후 토큰 재생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        response.addHeader("AccessToken", tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());

        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }



    private void validatePassword(LoginRequestDto loginRequestDto, User user) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RequestException(ExceptionType.LOGIN_FAIL_EXCEPTION);
        }
    }

}
