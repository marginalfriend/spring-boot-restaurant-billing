package io.abun.wmb.Auth;

import io.abun.wmb.Auth.dto.AuthRequest;
import io.abun.wmb.Auth.dto.LoginResponse;
import io.abun.wmb.Auth.dto.RegisterResponse;
import io.abun.wmb.Auth.interfaces.AuthService;
import io.abun.wmb.Auth.interfaces.JwtService;
import io.abun.wmb.Auth.interfaces.RoleService;
import io.abun.wmb.Auth.interfaces.UserAccountRepository;
import io.abun.wmb.CustomerManagement.CustomerEntity;
import io.abun.wmb.CustomerManagement.CustomerService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    final UserAccountRepository userAccountRepository;
    final AuthenticationManager authenticationManager;
    final CustomerService customerService;
    final PasswordEncoder passwordEncoder;
    final RoleService roleService;
    final JwtService jwtService;

    @Value("${wmb.superadmin.username}")
    private String superAdminUsername;

    @Value("${wmb.superadmin.password}")
    private String superAdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(AuthRequest request) {
        RoleEntity roleEntity = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserAccountEntity account = UserAccountEntity.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .isEnable(true)
                .roleEntity(List.of(roleEntity))
                .build();

        CustomerEntity customer = CustomerEntity.builder()
                .isMember(true)
                .userAccount(account)
                .build();

        customerService.create(customer.toRecord());
        userAccountRepository.saveAndFlush(account);

        return new RegisterResponse(
          account.getUsername(),
          account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }

    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        UserAccountEntity userAccount = (UserAccountEntity) authenticated.getPrincipal();

        String token = jwtService.generateToken(userAccount);

        LoginResponse loginResponse= new LoginResponse(
                userAccount.getUsername(),
                token,
                userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );

        log.info("Created Login Response");

        return loginResponse;
    }

    @Transactional
    @PostConstruct
    public void initSuperAdmin() {
        Optional<UserAccountEntity> superAdmin = userAccountRepository.findByUsername(superAdminUsername);
        
        if (superAdmin.isPresent()) {
            return;
        }

        RoleEntity superadmin = roleService.getOrSave(UserRole.ROLE_SUPER_ADMIN);
        RoleEntity admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        RoleEntity customer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        
        UserAccountEntity superAdminAccount = UserAccountEntity.builder()
                .username(superAdminUsername)
                .password(superAdminPassword)
                .roleEntity(List.of(superadmin, admin, customer))
                .isEnable(true)
                .build();
        
        userAccountRepository.save(superAdminAccount);
    }
}
