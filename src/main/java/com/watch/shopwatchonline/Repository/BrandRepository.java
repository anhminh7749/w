package com.watch.shopwatchonline.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.watch.shopwatchonline.Domain.BrandDto;
import com.watch.shopwatchonline.Model.Brand;

@Repository
public interface BrandRepository  extends JpaRepository<Brand, Integer>{
}