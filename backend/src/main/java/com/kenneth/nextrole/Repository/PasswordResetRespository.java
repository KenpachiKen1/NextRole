package com.kenneth.nextrole.Repository;


import com.kenneth.nextrole.Model.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRespository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByEmail(String email);
    void deleteByEmail(String email);
}
