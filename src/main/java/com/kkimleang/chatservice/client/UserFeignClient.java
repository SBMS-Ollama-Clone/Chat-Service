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
    @CircuitBreaker(name = "content", fallbackMethod = "fallbackGetUserProfile")
    @Retry(name = "content")
    Response<UserResponse> getUserProfile(@PathVariable UUID userId);

    default Response<UserResponse> fallbackGetUserProfile(UUID userId, Throwable throwable) {
        log.info("Fallback method for getUserProfile(UUID userId) in UserFeignClient: " + throwable.getMessage());
        return Response.<UserResponse>notFound().setErrors(throwable.getMessage());
    }

}

