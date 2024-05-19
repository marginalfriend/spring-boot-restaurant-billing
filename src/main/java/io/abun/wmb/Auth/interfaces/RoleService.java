package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.Role;
import io.abun.wmb.Auth.UserRole;

public interface RoleService {
    Role getOrSave(UserRole role);
}
