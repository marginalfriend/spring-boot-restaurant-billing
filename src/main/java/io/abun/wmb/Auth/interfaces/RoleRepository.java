package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.RoleEntity;
import io.abun.wmb.Auth.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByRole(UserRole role);
}
