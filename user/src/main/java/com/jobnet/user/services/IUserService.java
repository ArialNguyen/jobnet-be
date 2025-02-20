package com.jobnet.user.services;

import com.jobnet.clients.user.dtos.requests.UpgradeRequest;
import com.jobnet.user.dtos.requests.VerificationRequest;
import com.jobnet.common.dtos.UserResponse;
import com.jobnet.user.models.VerificationOTP;

public interface IUserService {

    VerificationOTP verifyUser(VerificationRequest request);

    UserResponse upgradeUser(UpgradeRequest request);
}
