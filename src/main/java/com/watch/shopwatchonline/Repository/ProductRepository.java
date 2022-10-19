package com.watch.shopwatchonline.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.watch.shopwatchonline.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{


    @Query(value = "SELECT p FROM Product p "+
   "join Brand b on b.id =  p.Brand.id "+ 
     "join Category c on c.id =  p.Category.id "+ 
    "where p.name like %:kw% or p.id like %:kw% or c.name like %:kw% or b.name like %:kw%")
    Page<Product> findByNameContaining(@Param("kw") String keyword, Pageable pageable);




// @Query(value = "INSERT INTO list_image VALUES (?1,?2,?3,?4,?5) select scope_identity() " ,nativeQuery = true)
// Integer searchListImage(String img1, String img2, String img3, String img4, String img5 );


@Query(value = "select p.* from product p where p.brand_id = ?1 and p.category_id = ?2 and p.price between ?3 and ?4 ;", nativeQuery = true)
Page<Product> findByAll(int id_brand, int id_cate, float min, float max, Pageable pageable);

@Query(value = "select p.* from product p where p.brand_id = ?1 and p.category_id = ?2 and p.price between ?3 and ?4 ;", nativeQuery = true)
Page<Product> findByAllNotBrand( int id_cate, float min, float max, Pageable pageable);

@Query(value = "select p.* from product p where p.brand_id = ?1 and p.category_id = ?2 and p.price between ?3 and ?4 ;", nativeQuery = true)
Page<Product> findByAllNotCate(int id_brand, float min, float max, Pageable pageable);

Page<Product> findByPriceBetween(double min, double max, Pageable pageable);
}
