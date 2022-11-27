package com.watch.shopwatchonline.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.watch.shopwatchonline.Model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT o.* FROM orders o " +
            "JOIN address a ON a.id = o.address_id " +
            "JOIN users u ON u.id = a.user_id " +
            "WHERE u.id = :uid " +
            "ORDER BY o.create_at DESC", nativeQuery = true)
    List<Order> FindbyUserName(@Param("uid") int userId);

    @Query(value = "SELECT * FROM orders WHERE status = :tt", nativeQuery = true)
    List<Order> findByStatus(@Param("tt") Short st);

    @Query(value = "SELECT * FROM orders WHERE status = 4 OR status = 5", nativeQuery = true)
    List<Order> findByStatusCancel();
   
    // @Query(value = "DELETE o FROM orders o " +
    // "JOIN address a ON a.id = o.address_id " +
    // "JOIN users u ON u.id = a.user_id " +
    // "WHERE u.id = :uid ", nativeQuery = true)
    // List<Order> DelbyUserName(@Param("uid") int userId);

}
