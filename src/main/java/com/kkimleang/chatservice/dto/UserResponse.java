package com.kkimleang.chatservice.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private Set<RoleResponse> roles;
    private Set<PermissionResponse> permissions;
}
