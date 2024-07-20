package com.ajay.repository;

import com.ajay.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Wallet finByUserId(Long id);
}
