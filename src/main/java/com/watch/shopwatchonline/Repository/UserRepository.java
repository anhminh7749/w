package com.watch.shopwatchonline.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.watch.shopwatchonline.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  @Query(value = "SELECT * FROM users WHERE username = ?1",nativeQuery = true)
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}

