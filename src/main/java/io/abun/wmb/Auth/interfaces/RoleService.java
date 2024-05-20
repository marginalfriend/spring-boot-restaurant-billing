package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.RoleEntity;
import io.abun.wmb.Auth.UserRole;

public interface RoleService {
    RoleEntity getOrSave(UserRole role);
}
