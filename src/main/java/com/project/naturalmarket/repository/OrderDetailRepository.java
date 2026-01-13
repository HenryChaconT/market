package com.project.naturalmarket.repository;

import com.project.naturalmarket.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

    @Query("SELECT p FROM OrderDetail p WHERE p.orders = :id")
    List<OrderDetail> findOrderDetailsByOrderId (@Param("id") Long id);


}
