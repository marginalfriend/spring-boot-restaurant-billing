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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final RoleService roleService;
    final PasswordEncoder passwordEncoder;
    final UserAccountRepository userAccountRepository;
    final CustomerService customerService;
    final AuthenticationManager authenticationManager;
    final JwtService jwtService;

    @Override
    @Transactional
    public RegisterResponse register(AuthRequest request) {
        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        String hashedPassword = passwordEncoder.encode(request.password());

        UserAccount account = UserAccount.builder()
                .username(request.username())
                .password(hashedPassword)
                .role(List.of(role))
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

        Authentication authRequest = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );

        Authentication authenticated = authenticationManager.authenticate(authRequest);

        UserAccount userAccount = (UserAccount) authenticated.getPrincipal();

        String token = jwtService.generateToken(userAccount);

        return new LoginResponse(
                userAccount.getUsername(),
                token,
                userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }
}
