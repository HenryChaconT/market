package com.project.naturalmarket.controller;

import com.project.naturalmarket.dto.ProductDto;
import com.project.naturalmarket.dto.ProductResponse;
import com.project.naturalmarket.entity.Product;
import com.project.naturalmarket.service.ProductService;
import com.project.naturalmarket.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;


@RequestMapping("/api/product")
@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) throws IOException{

        /*byte[] imagenBytes = Base64.getDecoder().decode(productDto.getImagenBase64());
        String base64=Base64.getEncoder().encodeToString(imagenBytes);
        productDto.setImagenBase64(base64);*/

        return new ResponseEntity<>(productService.create(productDto), HttpStatus.CREATED);

    }

    @GetMapping("/")
    public ProductResponse getAll(@RequestParam(name = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false)int pageNo,
                                  @RequestParam(name = "pageSize",defaultValue =AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,
                                  @RequestParam(name = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
                                  @RequestParam(name = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false)String sortDir){

        return productService.getAll(pageNo,pageSize,sortBy,sortDir);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable(name = "id") long id){

        return ResponseEntity.ok(productService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,
                                                    @PathVariable(name = "id")long id){

        return new ResponseEntity<>(productService.updateProduct(productDto,id),HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>  deleteProduct(@PathVariable(name = "id") long id){

        productService.deleteProduct(id);
        return new ResponseEntity<>("Se elimino correctamente",HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDto>> getProductByCategoryId(@PathVariable(name = "id") long id){

        List<ProductDto> products= productService.getByIdCategory(id);

        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity<List<ProductDto>> getProductBySupplierId(@PathVariable(name = "id") long id){

        List<ProductDto> productDtoList=productService.getByIdSupplier(id);

        return new  ResponseEntity<>(productDtoList,HttpStatus.OK);
    }

}
