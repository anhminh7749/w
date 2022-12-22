package com.watch.shopwatchonline.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.watch.shopwatchonline.Model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query(value = "select * FROM address where address.status = ?1 and address.user_id = ?2 and delete_at is null", nativeQuery = true)
    Address findByStatus(Short status,int id);

    @Query(value = "select * FROM address where address.user_id = ?1 and delete_at is null", nativeQuery = true)
    List<Address> findbyuser(int id);
}
