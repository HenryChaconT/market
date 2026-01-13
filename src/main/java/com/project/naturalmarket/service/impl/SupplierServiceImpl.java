package com.project.naturalmarket.service.impl;

import com.project.naturalmarket.dto.ProductDto;
import com.project.naturalmarket.dto.SupplierDto;
import com.project.naturalmarket.entity.Product;
import com.project.naturalmarket.entity.Supplier;
import com.project.naturalmarket.exception.ResourceNotFoundException;
import com.project.naturalmarket.repository.ProductRepository;
import com.project.naturalmarket.repository.SupplierRepository;
import com.project.naturalmarket.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepository supplierRepository;
    private ProductRepository productRepository;
    private ModelMapper mapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper mapper,ProductRepository productRepository) {
        this.supplierRepository = supplierRepository;
        this.mapper = mapper;
        this.productRepository=productRepository;
    }

    @Override
    public SupplierDto create(SupplierDto supplierDto) {


        Supplier supplier=mapper.map(supplierDto,Supplier.class);
        Supplier supplier1 = supplierRepository.save(supplier);

        return mapper.map(supplier1,SupplierDto.class);
    }

    @Override
    public List<SupplierDto> getSuppliers() {


        List<Supplier> supplierList=supplierRepository.findAll();

        List<SupplierDto> supplierDtos= supplierList.stream().map(supplier -> mapper.map(supplier,SupplierDto.class)).collect(Collectors.toList());


        supplierDtos.forEach(supplier -> {
            if (productRepository.existsBySupplierId(supplier.getId())) {
                 List<Product> product= productRepository.findProductBySupplierId(supplier.getId());
                List<ProductDto>productDto= product.stream().map(product1 -> mapper.map(product1,ProductDto.class)).collect(Collectors.toList());
                supplier.setProduct(productDto);
            }
        });


        return supplierDtos;

    }

    @Override
    public SupplierDto getById(long id) {

        Supplier supplier=supplierRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Suplier","id",id));

        return mapper.map(supplier,SupplierDto.class);
    }

    @Override
    public SupplierDto update(SupplierDto supplierDto, long id) {

        Supplier supplier=supplierRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Suplier","id",id));

        supplier.setName(supplierDto.getName());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setPhone(supplierDto.getPhone());
        supplier.setEmail(supplierDto.getEmail());

        Supplier supplier1=supplierRepository.save(supplier);

        return mapper.map(supplier1,SupplierDto.class);
    }

    @Override
    public void delete(long id) {

        Supplier supplier=supplierRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Suplier","id",id));

        supplierRepository.delete(supplier);
    }
}
