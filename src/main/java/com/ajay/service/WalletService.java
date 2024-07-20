package com.ajay.service;

import com.ajay.model.Order;
import com.ajay.model.User;
import com.ajay.model.Wallet;

public interface WalletService {
    Wallet getUserWallet (User user);

    Wallet addBalance(Wallet wallet, Long money);

    Wallet findById(Long id) throws Exception;

    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;

    Wallet payOrderPayment(Order order, User user) throws Exception;

}
