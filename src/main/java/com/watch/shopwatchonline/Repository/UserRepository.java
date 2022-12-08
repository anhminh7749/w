package com.watch.shopwatchonline.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.watch.shopwatchonline.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  @Query(value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
  Optional<User> findByUsername(String username);

  @Query(value = "SELECT * FROM users WHERE email = ?1 and id != ?2", nativeQuery = true)
  Optional<User> findByEmail(String email, int id);

  @Query(value = "SELECT * FROM users WHERE phone = ?1 and id != ?2", nativeQuery = true)
  Optional<User> findByPhone(String phone, int id);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  @Query(value = "SELECT count(u.id)  from users u", nativeQuery = true)
  Integer countUsers();
}
