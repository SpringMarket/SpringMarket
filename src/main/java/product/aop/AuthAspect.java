package product.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import product.exception.RequestException;
import product.repository.user.UserRepository;

import static product.exception.ExceptionType.NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Aspect
@Component
public class AuthAspect {
    private final UserRepository userRepository;

    @Before("@annotation(shop.product.aop.annotation.Auth)")
    public void checkAdminRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
    }
}
