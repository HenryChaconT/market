package com.project.naturalmarket.service.impl;

import com.project.naturalmarket.dto.ProductDto;
import com.project.naturalmarket.dto.ProductResponse;
import com.project.naturalmarket.entity.Category;
import com.project.naturalmarket.entity.Product;
import com.project.naturalmarket.entity.Supplier;
import com.project.naturalmarket.exception.ResourceNotFoundException;
import com.project.naturalmarket.repository.CategoryRepository;
import com.project.naturalmarket.repository.ProductRepository;
import com.project.naturalmarket.repository.SupplierRepository;
import com.project.naturalmarket.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductServiceImpl implements ProductService {


    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;

    private SupplierRepository supplierRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper,
                              SupplierRepository supplierRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository=categoryRepository;
        this.supplierRepository=supplierRepository;
    }

    @Override
    public ProductDto create(ProductDto productDto) throws IOException {

        String imagenBase64 = productDto.getImagenBase64();

        // Convertir la cadena base64 a un arreglo de bytes
        byte[] imagenBytes = Base64.getDecoder().decode(imagenBase64);


        Category category=categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
                ()-> new ResourceNotFoundException("Category","id", productDto.getCategoryId()));

        Supplier supplier=supplierRepository.findById(productDto.getSupplierId()).orElseThrow(
                () -> new ResourceNotFoundException("Supplier","id", productDto.getSupplierId()));

        Product product = modelMapper.map(productDto,Product.class);
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setImagen(imagenBytes);

        Product product1 = productRepository.save(product);

        ProductDto productDto1=modelMapper.map(product1,ProductDto.class);
        String imagenResponse= Base64.getEncoder().encodeToString(product.getImagen());
        productDto1.setImagenBase64(imagenResponse);

        return productDto1;
    }

    @Override
    public ProductResponse getAll(int pageNo,int pageSize,String sortBy, String sortDir) {

        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);

        Page<Product> products=productRepository.findAll(pageable);

        List<Product> productList=products.getContent();

        List<ProductDto> content=productList.stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());

        //Se setea cada imagen de byte a String0
        List<Integer> indices = IntStream.range(0, content.size()).boxed().toList();
        indices.forEach(i -> {
            String imagenBase64 = Base64.getEncoder().encodeToString(productList.get(i).getImagen());
            content.get(i).setImagenBase64(imagenBase64);
        });

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPage(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductDto getById(long id) {

        Product product=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product","id",id));

        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, long id) {

        Product product=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product","id",id));

        Category category=categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("category","id", productDto.getCategoryId()));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setCategory(category);


        productRepository.save(product);

        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public void deleteProduct(long id) {

        Product product=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product","id",id));

        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getByIdCategory(long id) {

        Category category=categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category","id",id));

        List<Product> products=productRepository.findProductByCategoryId(id);


        return products.stream().map(product1 -> modelMapper.map(product1,ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getByIdSupplier(long id) {

        Supplier supplier=supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier","id", id));

        List<Product> productList=productRepository.findProductBySupplierId(id);

        return productList.stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());
    }


}