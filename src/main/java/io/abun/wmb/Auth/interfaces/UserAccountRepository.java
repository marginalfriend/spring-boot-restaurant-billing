package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
}