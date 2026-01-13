package com.project.naturalmarket.repository;


import com.project.naturalmarket.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Long> {

    boolean existsById(long id);
}
