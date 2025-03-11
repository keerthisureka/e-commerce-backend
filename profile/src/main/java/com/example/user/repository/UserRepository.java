package com.example.user.repository;

import com.example.user.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Profile, String> {
    boolean existsByEmail(String incomingEmail);

    Profile findByEmail(String incomingEmail);
}
