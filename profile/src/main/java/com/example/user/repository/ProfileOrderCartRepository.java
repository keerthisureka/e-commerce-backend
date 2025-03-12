package com.example.user.repository;

import com.example.user.entity.ProfileOrderCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileOrderCartRepository extends JpaRepository<ProfileOrderCart, String> {
}
