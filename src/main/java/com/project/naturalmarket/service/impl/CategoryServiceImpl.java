package com.project.naturalmarket.service.impl;

import com.project.naturalmarket.dto.CategoryDto;
import com.project.naturalmarket.entity.Category;
import com.project.naturalmarket.exception.ResourceNotFoundException;
import com.project.naturalmarket.repository.CategoryRepository;
import com.project.naturalmarket.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper=mapper;
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        Category category=mapper.map(categoryDto,Category.class);
        Category category1= categoryRepository.save(category);

        return mapper.map(category1,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        List<Category> categories=categoryRepository.findAll();

        return categories.stream().map(category -> mapper.map(category,CategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long id) {

        Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category","id",id));


        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, long id) {

        Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category","id",id));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category savedCategory=categoryRepository.save(category);

        return mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public void delete(long id) {

        Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category","id",id));

        categoryRepository.delete(category);
    }


}
