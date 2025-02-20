package com.jobnet.user.dtos.responses;

import com.jobnet.user.models.enums.EGender;
import com.jobnet.common.dtos.ERole;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(of = {"id", "name"})
public class AdminResponse{
    private String id;
    private String email;
    private String name;
    private ERole role;
    private EGender gender;
    private String phone;
    private LocalDate dateOfBirth;
}
