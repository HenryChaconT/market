package com.project.naturalmarket.service;

import com.project.naturalmarket.dto.ProductDto;
import com.project.naturalmarket.dto.ProductResponse;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto productDto) throws IOException;

    ProductResponse getAll(int pageNo,int pageSize,String sortBy, String sortDir);

    ProductDto getById(long id);

    ProductDto updateProduct(ProductDto productDto,long id);

    void deleteProduct (long id);

    List<ProductDto> getByIdCategory(long id);

    List<ProductDto> getByIdSupplier(long id);
}
