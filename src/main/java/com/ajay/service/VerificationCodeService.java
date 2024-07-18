package com.ajay.service;

import com.ajay.domain.VerificationType;
import com.ajay.model.User;
import com.ajay.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode  getVerificationCodeById(Long id ) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);


}
