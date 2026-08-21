package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Customer;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.UserRepository;
import com.kenneth.nextrole.SubscriptionStatus;
import com.kenneth.nextrole.dto.user.UpdateUserRequest;
import com.kenneth.nextrole.dto.user.UserResponse;
import com.kenneth.nextrole.exception.EmailAlreadyExistsException;
import com.kenneth.nextrole.exception.UsernameAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .id(1L)
                .username("kenneth")
                .email("kenneth@example.com")
                .firstName("Kenneth")
                .lastName("Smith")
                .password("hashed-password")
                .customer(Customer.builder().subscriptionStatus(SubscriptionStatus.FREE).build())
                .build();
    }

    @Test
    void updateCurrentUser_updatesUsername_whenNewUsernameIsAvailable() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("newname");

        when(userRepository.findByEmail("kenneth@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsernameIgnoreCase("newname")).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserResponse response = userService.updateCurrentUser(request, "kenneth@example.com");

        assertThat(existingUser.getUsername()).isEqualTo("newname");
        verify(userRepository).save(existingUser);
        assertThat(response).isNotNull();
    }

    @Test
    void updateCurrentUser_throwsUsernameAlreadyExists_whenUsernameTakenByAnotherUser() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("takenname");

        when(userRepository.findByEmail("kenneth@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsernameIgnoreCase("takenname")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateCurrentUser(request, "kenneth@example.com"))
                .isInstanceOf(UsernameAlreadyExistsException.class);

        // username should be untouched since the update was rejected
        assertThat(existingUser.getUsername()).isEqualTo("kenneth");
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateCurrentUser_allowsSameUsername_whenUserKeepsTheirOwnName() {
        // existingUser.getUsername() is "kenneth" from setUp()
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("Kenneth"); // same name, different case

        when(userRepository.findByEmail("kenneth@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsernameIgnoreCase("Kenneth")).thenReturn(true);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updateCurrentUser(request, "kenneth@example.com");

        // should NOT throw, because it's the user's own existing username
        assertThat(existingUser.getUsername()).isEqualTo("Kenneth");
    }

    @Test
    void updateCurrentUser_throwsEmailAlreadyExists_whenEmailTakenByAnotherUser() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("taken@example.com");

        when(userRepository.findByEmail("kenneth@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateCurrentUser(request, "kenneth@example.com"))
                .isInstanceOf(EmailAlreadyExistsException.class);

        assertThat(existingUser.getEmail()).isEqualTo("kenneth@example.com");
    }

    @Test
    void updateCurrentUser_throwsEntityNotFound_whenUserDoesNotExist() {
        UpdateUserRequest request = new UpdateUserRequest();

        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateCurrentUser(request, "ghost@example.com"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteAccount_deletesUser_whenFound() {
        when(userRepository.findByEmail("kenneth@example.com"))
                .thenReturn(Optional.of(existingUser));

        userService.deleteAccount("kenneth@example.com");

        verify(userRepository).delete(existingUser);
    }

    @Test
    void deleteAccount_throwsEntityNotFound_whenUserMissing() {
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteAccount("ghost@example.com"))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository, never()).delete(any());
    }
}