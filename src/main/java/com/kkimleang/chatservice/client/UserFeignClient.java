package com.kkimleang.chatservice.client;

import com.kkimleang.chatservice.config.FeignClientConfig;
import com.kkimleang.chatservice.dto.Response;
import com.kkimleang.chatservice.dto.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.logging.Logger;

@FeignClient(name = "AUTH-SERVICE", configuration = FeignClientConfig.class)
public interface UserFeignClient {
    Logger log = Logger.getLogger(UserFeignClient.class.getName());

    @GetMapping("/api/auth/user/{userId}/profile")
    @CircuitBreaker(name = "user-profile", fallbackMethod = "fallbackGetUserProfile")
    @Retry(name = "user-profile")
    Response<UserResponse> getUserProfile(@PathVariable UUID userId);

    @GetMapping("/api/auth/user/me")
    @CircuitBreaker(name = "user-info", fallbackMethod = "fallbackGetUserInfo")
    @Retry(name = "user-info")
    Response<UserResponse> getMyProfile();

    default Response<UserResponse> fallbackGetUserProfile(UUID userId, Throwable throwable) {
        log.info("Fallback method for getUserProfile(UUID userId) in UserFeignClient: " + throwable.getMessage());
        return Response.<UserResponse>notFound().setErrors(throwable.getMessage());
    }

    default Response<UserResponse> fallbackGetUserInfo(Throwable throwable) {
        log.info("Fallback method for getMyProfile() in UserFeignClient: " + throwable.getMessage());
        return Response.<UserResponse>notFound().setErrors(throwable.getMessage());
    }
}

