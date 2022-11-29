package com.watch.shopwatchonline.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.watch.shopwatchonline.Model.ChatBox;

@Repository
public interface ChatBoxRepository extends JpaRepository<ChatBox, Integer>{
    @Query(value = "SELECT * FROM chat_box JOIN users ON users.id = chat_box.userid WHERE users.username = ?1",nativeQuery = true)
    Optional<ChatBox> findByUsername(String username);
}
