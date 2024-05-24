package io.abun.wmb.Auth;

import io.abun.wmb.Auth.interfaces.UserAccountRepository;
import io.abun.wmb.Auth.interfaces.UserAccountService;
import io.abun.wmb.Constants.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccountEntity loadUserById(UUID id) {
        return userAccountRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
    }

    @Override
    public UserAccountEntity getByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userAccountRepository.findByUsername(authentication.getPrincipal().toString()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.AUTHENTICATION_FAILURE + " : " + Messages.NOT_FOUND)
        );
    }
}
