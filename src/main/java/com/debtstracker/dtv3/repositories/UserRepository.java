package com.debtstracker.dtv3.repositories;

import com.debtstracker.dtv3.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findBySessionIdentifier(String sessionIdentifier);

    Optional<User> findBySessionIdentifierNot(String sessionIdentifier);

}
