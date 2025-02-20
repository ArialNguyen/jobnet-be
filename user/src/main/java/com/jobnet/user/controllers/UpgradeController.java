package com.jobnet.user.controllers;

import com.jobnet.clients.user.dtos.requests.UpgradeRequest;
import com.jobnet.common.dtos.UserResponse;
import com.jobnet.user.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/upgrade")
@RequiredArgsConstructor
public class UpgradeController {

    private final IUserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse upgradeUser(@RequestBody @Valid UpgradeRequest request) {
        return userService.upgradeUser(request);
    }
}
