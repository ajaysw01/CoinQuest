package com.ajay.controller;

import com.ajay.Utils.OtpUtils;
import com.ajay.config.JwtProvider;
import com.ajay.model.TwoFactorOTP;
import com.ajay.model.User;
import com.ajay.repository.UserRepository;
import com.ajay.response.AuthResponse;
import com.ajay.service.CustomUserDetailsService;
import com.ajay.service.EmailService;
import com.ajay.service.TwoFactorOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOTPService twoFactorOTPService;

    @Autowired
    private EmailService emailService;

    @PostMapping("signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new Exception("Email already exists, please try with another email");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFullname(user.getFullname());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

        User savedUser = userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        // create jwt token
        String jwt = JwtProvider.generateToken(auth);


        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Registered Successfully");

        //send to frontend
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }



    @PostMapping("signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        //aceess user and password from RequestBoddy
        String userName = user.getEmail();
        String password = user.getPassword();

        //create authentication using authenticate method
        Authentication auth = authenticate(userName, password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        // create jwt token
        String jwt = JwtProvider.generateToken(auth);


        User authUser = userRepository.findByEmail(userName);
        //checking if two factor authentication is enabled or not
        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor Auth is Enabled");
            res.setTwoFactorAuthEnabled(true);

            //generate the otp
            String otp = OtpUtils.generateOtp();

            //create two factor otp
            TwoFactorOTP oldTwoFactorOTP = twoFactorOTPService.findByUser(authUser.getId());
            if(oldTwoFactorOTP != null){
                twoFactorOTPService.deleteTwoFactorOTP(oldTwoFactorOTP);
            }

            // create new otp
            TwoFactorOTP newTwoFactorOTP = twoFactorOTPService.createTwoFactorOTP(authUser,otp,jwt);//creating new otp

           // sending email to user with this otp
            emailService.sendVerificationOtpEmail(userName,otp);

            //getting id and setting it as a session
            res.setSession(newTwoFactorOTP.getId());
            return  new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }
        //if two factor auth is not enabled;
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login Successfully");

        //send to frontend
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }//login

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySiginOtp(
            @PathVariable String otp,
            @RequestParam String id) throws Exception {//twofactorotp

        TwoFactorOTP twoFactorOTP = twoFactorOTPService.findById(id);

        if(twoFactorOTPService.verifyTwoFactorOTP(twoFactorOTP,otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor authentication verifies");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("Invalid Otp");
    }



    private Authentication authenticate(String userName, String password) {

        //checking if user exist or not
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

        if (userDetails == null) {
                throw new BadCredentialsException("Invalid Username");
            }
            if (!password.equals(userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid Password");
            }
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        }

    }