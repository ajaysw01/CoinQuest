package com.ajay.service;

import com.ajay.config.JwtProvider;
import com.ajay.domain.VerificationType;
import com.ajay.model.TwoFactorAuth;
import com.ajay.model.User;
import com.ajay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw  new Exception("User Not Found");
        }
        return  user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw  new Exception("User Not Found");
        }
        return  user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);;
        if(user.isEmpty()){
            throw new Exception("user not found");
        }

        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);//enable two fact auth
        twoFactorAuth.setSendTo(verificationType);//on which method we want otp mobile or email

        user.setTwoFactorAuth(twoFactorAuth);

        return  userRepository.save(user);
    }



    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
