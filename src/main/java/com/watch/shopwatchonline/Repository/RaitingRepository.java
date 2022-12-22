package com.watch.shopwatchonline.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watch.shopwatchonline.Model.Raiting;

public interface RaitingRepository extends JpaRepository<Raiting, Integer> {
    @Query(value = "SELECT * FROM Raiting " +
            " JOIN Order_Detail ON Order_Detail.raiting_Id = Raiting.id " +
            " where Order_Detail.product_Id = :pid", nativeQuery = true)
    List<Raiting> findByProductId(@Param("pid") int id_pro);

    @Query(value = " SELECT AVG(point) FROM Raiting " +
            " JOIN Order_Detail ON Order_Detail.raiting_Id = Raiting.id " +
            " where Order_Detail.product_Id = :pid", nativeQuery = true)
    String AvgByProductId(@Param("pid") int id_pro);

    @Query(value = " SELECT COUNT(point) FROM Raiting " +
            " JOIN Order_Detail ON Order_Detail.raiting_Id = Raiting.id " +
            " where Order_Detail.product_Id = :pid", nativeQuery = true)
    String CountByProductId(@Param("pid") int id_pro);

    @Query(value = "SELECT * FROM Raiting  " +
            "join users u on u.id = Raiting.user_id " +
            "JOIN Order_Detail ON Order_Detail.raiting_Id = Raiting.id " +
            "join product p on p.id =  Order_Detail.product_id " +
        "where (u.username like %:kw% or p.name like %:kw% ) and Raiting.active = :act", nativeQuery = true)
Page<Raiting> findByKeyWord(@Param("kw") String keyword, @Param("act") Short active, Pageable pageable);

Page<Raiting> findByActive(Short active, Pageable pageable);

    @Query(value = " SELECT AVG(point) FROM Raiting ", nativeQuery = true)
    Integer avgRaitting();

    @Query(value = "SELECT * FROM Raiting  " +
            "join users u on u.id = Raiting.user_id " +
            "JOIN Order_Detail ON Order_Detail.raiting_Id = Raiting.id "+
            "where Order_Detail.id=:id", nativeQuery = true)
    Optional<Raiting> findByUserAndDetail(@Param("id") int id);

//     @Query(value = "SELECT sum(point),user_id FROM Raiting Group BY user_id", nativeQuery = true)
//     Integer findByUserAndPoint();

@Query(value = " SELECT SUM(point) FROM Raiting WHERE point = ?1", nativeQuery = true)
Integer avgRaittingforPoint(int point);
}
