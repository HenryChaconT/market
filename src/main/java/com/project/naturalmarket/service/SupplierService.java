package com.project.naturalmarket.service;

import com.project.naturalmarket.dto.SupplierDto;

import java.util.List;

public interface SupplierService {

    SupplierDto create (SupplierDto supplierDto);
    List<SupplierDto> getSuppliers();
    SupplierDto getById(long id);
    SupplierDto update(SupplierDto supplierDto, long id);
    void delete(long id);

}
