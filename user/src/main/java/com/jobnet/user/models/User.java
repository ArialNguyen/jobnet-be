package com.jobnet.user.models;

import com.jobnet.common.dtos.ERole;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private String email;
    private String password;
    private String name;
    private ERole role;
    private Boolean locked;
    private Boolean enabled;
    private Boolean upgraded;
}
