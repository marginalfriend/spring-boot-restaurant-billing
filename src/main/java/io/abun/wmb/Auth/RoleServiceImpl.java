package io.abun.wmb.Auth;

import io.abun.wmb.Auth.interfaces.RoleRepository;
import io.abun.wmb.Auth.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    final RoleRepository repository;

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
