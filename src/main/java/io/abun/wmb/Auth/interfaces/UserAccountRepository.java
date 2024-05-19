package io.abun.wmb.Auth.interfaces;

import io.abun.springbootikamers.Auth.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
}