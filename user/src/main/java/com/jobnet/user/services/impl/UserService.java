package com.jobnet.user.services.impl;

import com.jobnet.common.exceptions.ResourceNotFoundException;
import com.jobnet.common.i18n.MessageUtil;
import com.jobnet.clients.user.dtos.requests.UpgradeRequest;
import com.jobnet.user.dtos.requests.VerificationRequest;
import com.jobnet.common.dtos.UserResponse;
import com.jobnet.user.models.JobSeeker;
import com.jobnet.user.models.User;
import com.jobnet.user.models.VerificationOTP;
import com.jobnet.common.dtos.ERole;
import com.jobnet.user.models.enums.EVerificationStatus;
import com.jobnet.user.repositories.JobSeekerRepository;
import com.jobnet.user.repositories.UserRepository;
import com.jobnet.user.services.IUserService;
import com.jobnet.user.services.IVerificationOTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final JobSeekerRepository jobSeekerRepository;
	private final IVerificationOTPService confirmationOTPService;
	private final MessageUtil messageUtil;

	@Override
	public VerificationOTP verifyUser(VerificationRequest request) {
		User user = findByIdOrElseThrow(request.getUserId());

		VerificationOTP verificationOTP =
			confirmationOTPService.updateVerificationOTPConfirmedAt(user, request.getOtpToken());

		if (user.getRole().equals(ERole.JobSeeker)) {
			JobSeeker jobSeeker = jobSeekerRepository.findByEmail(user.getEmail())
									  .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("error.notFound.user")));
			jobSeeker.setVerificationStatus(EVerificationStatus.Verified);
			jobSeeker.setEnabled(true);
			jobSeekerRepository.save(jobSeeker);
		} else {
			user.setEnabled(true);
			userRepository.save(user);
		}
		return verificationOTP;
	}

	@Override
	public UserResponse upgradeUser(UpgradeRequest request) {
		User user = findByIdOrElseThrow(request.getUserId());

		if (request.getAction().equals(UpgradeRequest.Action.UPGRADE_USER)) {
			if (BooleanUtils.isTrue(user.getUpgraded()))
				throw new IllegalArgumentException("User is already upgraded");

			user.setUpgraded(true);
			user = userRepository.save(user);
			log.info("Account is upgraded");
			return UserResponse.builder()
					.id(user.getId())
					.email(user.getEmail())
					.name(user.getName())
					.role(user.getRole())
					.upgraded(user.getUpgraded())
					.build();
		}
		throw new IllegalArgumentException("Action argument is illegal");
	}

	private User findByIdOrElseThrow(String userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("error.notFound.user")));
	}
}
