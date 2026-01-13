package com.project.naturalmarket.service;

import com.project.naturalmarket.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    List<CategoryDto> getAllCategories();

    CategoryDto getById(long id);

    CategoryDto update(CategoryDto categoryDto,long id);

    void delete(long id);
}
