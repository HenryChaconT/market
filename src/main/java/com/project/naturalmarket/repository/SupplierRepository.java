package com.project.naturalmarket.repository;

import com.project.naturalmarket.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}