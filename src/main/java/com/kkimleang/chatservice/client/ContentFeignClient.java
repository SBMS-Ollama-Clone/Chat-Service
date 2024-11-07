package com.kkimleang.chatservice.client;

import com.kkimleang.chatservice.dto.ContentResponse;
import com.kkimleang.chatservice.dto.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@FeignClient(name = "CONTENT-SERVICE")
public interface ContentFeignClient {
    Logger log = Logger.getLogger(ContentFeignClient.class.getName());

    @GetMapping("/api/contents/{chatId}/all")
    @CircuitBreaker(name = "content", fallbackMethod = "fallbackGetAllContentsByChatId")
    @Retry(name = "content")
    Response<List<ContentResponse>> getAllContentsByChatId(@PathVariable UUID chatId);

    default Response<List<ContentResponse>> fallbackGetAllContentsByChatId(UUID chatId, Throwable throwable) {
        log.info("Fallback method for getAllContentsByChatId(UUID chatId) in ContentFeignClient: " + throwable.getMessage());
        return Response.<List<ContentResponse>>notFound().setErrors(throwable.getMessage());
    }

    @DeleteMapping("/api/contents/{chatId}/delete/all")
    void deleteContentsByChatId(@PathVariable UUID chatId);
}
