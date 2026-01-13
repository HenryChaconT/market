package com.project.naturalmarket.repository;

import com.project.naturalmarket.dto.ProductDto;
import com.project.naturalmarket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductByCategoryId(Long id);

    List<Product> findProductBySupplierId(Long id);


    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Set<Product> findProductBySupplierName(@Param("name") String name);

    Set<Product> findAllBySupplierName(String name);

    boolean existsBySupplierId(long id);
}