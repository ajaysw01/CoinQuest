package com.ajay.service;

import com.ajay.domain.VerificationType;
import com.ajay.model.ForgotPasswordToken;
import com.ajay.model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User uesr,
                                    String id,
                                    String otp,
                                    VerificationType verificationType,
                                    String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToke(ForgotPasswordToken token);

}
