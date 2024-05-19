package io.abun.wmb.Auth;

import io.abun.springbootikamers.Auth.dto.AuthRequest;
import io.abun.springbootikamers.Auth.dto.LoginResponse;
import io.abun.springbootikamers.Auth.dto.RegisterResponse;
import io.abun.springbootikamers.Auth.interfaces.AuthService;
import io.abun.springbootikamers.Auth.interfaces.JwtService;
import io.abun.springbootikamers.Auth.interfaces.RoleService;
import io.abun.springbootikamers.Auth.interfaces.UserAccountRepository;
import io.abun.springbootikamers.CustomerServices.CustomerEntity;
import io.abun.springbootikamers.CustomerServices.CustomerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    RoleService roleService;
    PasswordEncoder passwordEncoder;
    UserAccountRepository userAccountRepository;
    CustomerService customerService;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    @Override
    public RegisterResponse register(AuthRequest request) {
        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);

        String hashedPassword = passwordEncoder.encode(request.password());

        UserAccount account = UserAccount.builder()
                .username(request.username())
                .password(hashedPassword)
                .role(List.of(role))
                .build();

        userAccountRepository.saveAndFlush(account);

        CustomerEntity customer = CustomerEntity.builder()
                .membership(true)
                .account(account)
                .build();

        customerService.addNewCustomer(customer);

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
