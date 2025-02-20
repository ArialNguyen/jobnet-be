package com.jobnet.user.dtos.responses;

import com.jobnet.common.dtos.ERole;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(of = {"id", "name"})
public class RawRecruiterResponse {
    private String id;
    private String email;
    private String name;
    private ERole role;
    private String phone;
    private String profileImageId;
    private String nation;
    private boolean activeBusiness;
    private String businessId;
    private Boolean locked;
}
