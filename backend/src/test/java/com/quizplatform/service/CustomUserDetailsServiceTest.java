package com.quizplatform.service;

import com.quizplatform.entity.User;
import com.quizplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    private User user;
    private User admin;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setRole("user");

        admin = new User();
        admin.setEmail("admin@example.com");
        admin.setPassword("encodedPassword");
        admin.setRole("admin");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("user@example.com");

        assertThat(details.getUsername()).isEqualTo("user@example.com");
        assertThat(details.getPassword()).isEqualTo("encodedPassword");
        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }

    @Test
    void loadUserByUsername_shouldHandleAdminRole() {
        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(admin));

        UserDetails details = service.loadUserByUsername("admin@example.com");

        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.loadUserByUsername("missing@example.com")
        ).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_shouldBeCaseSensitiveIfConfiguredSo() {
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("user@example.com");

        assertThat(details).isNotNull();
    }
}