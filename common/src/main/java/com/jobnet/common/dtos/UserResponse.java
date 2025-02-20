package com.jobnet.common.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(of = {"id", "name"})
public class UserResponse {
    private String id;
    private String email;
    private String name;
    private ERole role;
    private Boolean upgraded;
}
