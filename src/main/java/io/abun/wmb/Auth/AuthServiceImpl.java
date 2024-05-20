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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public LoginResponse login(AuthRequest request) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        log.info("Created AuthRequest " + authentication);

        Authentication authenticated = authenticationManager.authenticate(authentication);

        log.info("Authenticated");

        UserAccountEntity userAccount = (UserAccountEntity) authenticated.getPrincipal();

        log.info("Got principal");

        String token = jwtService.generateToken(userAccount);

        log.info("Created Token");

        LoginResponse loginResponse= new LoginResponse(
                userAccount.getUsername(),
                token,
                userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );

        log.info("Created Login Response");

        return loginResponse;
    }
}
