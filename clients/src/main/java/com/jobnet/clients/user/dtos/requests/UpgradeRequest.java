package com.jobnet.clients.user.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpgradeRequest {

    @NotBlank(message = "User ID must not be blank.")
    private String userId;

    @NotBlank(message = "Action must not be blank.")
    private String action;

    public enum Action {
        UPGRADE_USER
    }

    public Action getAction() {
        return Action.valueOf(action);
    }
}
