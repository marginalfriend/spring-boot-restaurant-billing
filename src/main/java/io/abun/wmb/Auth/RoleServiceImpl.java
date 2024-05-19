package io.abun.wmb.Auth;

import io.abun.springbootikamers.Auth.interfaces.RoleRepository;
import io.abun.springbootikamers.Auth.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    RoleRepository repository;

    @Override
    public Role getOrSave(UserRole role) {
        return repository.findByRole(role).orElseGet(
                () -> repository.saveAndFlush(
                        Role.builder()
                                .role(role)
                                .build()
                )
        );
    }
}
