package com.ajay.controller;

import com.ajay.model.User;
import com.ajay.model.Wallet;
import com.ajay.model.Withdrawal;
import com.ajay.service.UserService;
import com.ajay.service.WalletService;
import com.ajay.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

   @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long amount
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
       Wallet userWallet =walletService.getUserWallet(user);

       Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount,user);
       walletService.addBalance(userWallet,-withdrawal.getAmount());

       return new  ResponseEntity<>(withdrawal , HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdrwal/{id}/proceed/{accept}")
    public ResponseEntity<?> processWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
       User user = userService.findUserProfileByJwt(jwt);
       Withdrawal withdrawal = withdrawalService.processWithdrawal(id, accept);

       Wallet userWallet = walletService.getUserWallet(user);

       //if admin decline then need to return to user
       if(!accept){
           walletService.addBalance(userWallet,withdrawal.getAmount());

       }

       return new ResponseEntity<>(withdrawal , HttpStatus.OK);

    }


    @GetMapping("/api/withdrawal")
    public  ResponseEntity<List<Withdrawal>> getWithdrawalHistory(
        @RequestHeader("Authorization ") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawals = withdrawalService.getUsersWithdrawalHistory(user);

        return  new ResponseEntity<>(withdrawals,HttpStatus.OK);

    }


    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequests(
            @RequestHeader("Authorization") String jwt) throws Exception {
       User user = userService.findUserProfileByJwt(jwt);

       List<Withdrawal> withdrawals=withdrawalService.getAllWithdrawalRequest();

       return  new ResponseEntity<>(withdrawals,HttpStatus.OK);
    }
}
